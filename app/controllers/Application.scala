package controllers

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.ws.Response
import play.api.libs.ws.WS
import play.api.mvc._
import oose.play.config.Configured
import util.AppConfig
import play.api.Routes

object Application extends Controller with Configured {

  implicit class MasterServer(val url: String) extends AnyVal

  lazy val appConfig = configured[AppConfig]

  implicit val url: MasterServer = appConfig.imageMaster

  class ProxyAction[A](bodyParser: BodyParser[A] = parse.anyContent)(ws: Request[A] => Future[Response], reply: Future[Response] => Future[SimpleResult])
    extends Action[A] {
    /**
     * compose the Action from the ws function and the reply function.
     */
    def apply(request: Request[A]): Future[SimpleResult] = {
      (reply compose ws)(request)
    }

    def parser = bodyParser
  }

  object ProxyAction {
    def apply[A](bodyParser: BodyParser[A] = parse.anyContent, ws: Request[A] => Future[Response], reply: Future[Response] => Future[SimpleResult]) = new ProxyAction(bodyParser)(ws, reply)
  }

  /**
   * make a GET request to the MasterServer /image URL.
   * @see [[ProxyAction]]
   */
  private def getImage[A](request: Request[A])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").get
  }

  /**
   * make a POST request, obtaining the MasterServer reply from the /image URL.
   * @see [[ProxyAction]]
   */
  private def postImage(request: Request[JsValue])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").post(request.body)
  }

  /**
   * Handle the response of a [[ProxyAction]].
   */
  private def response = {
    responseFut: Future[Response] =>
      responseFut.map(
        response =>
          response.status match {
            case 200 => Ok(response.body)
            case _ => ServiceUnavailable(response.body)
          }).recover {
          case ex: Throwable => BadRequest
        }
  }

  /**
   * display the UI view.
   */
  def index = Action {
    Ok(views.html.index())
  }

  /**
   *  request a new Image from the MasterServer.
   *
   */
  def image = ProxyAction(ws = getImage, reply = response)

  /**
   *  save the image tags to the MasterServer.
   */
  def saveData = ProxyAction(parse.json, ws = postImage, reply = response)

  def jsRoutes(varName: String = "jsRoutes") = Action { implicit request =>
    Ok(
      Routes.javascriptRouter(varName)(
        routes.javascript.Application.image,
        routes.javascript.Application.saveData)).as(JAVASCRIPT)
  }
}