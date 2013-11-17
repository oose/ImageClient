package controllers

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsValue
import play.api.libs.ws.Response
import play.api.libs.ws.WS
import play.api.mvc._
import oose.play.config.Configured
import oose.play.actions.ProxyAction
import oose.play.json.StatusMessage._
import util.AppConfig
import play.api.Routes

object Application extends Controller with Configured {

  implicit class MasterServer(val url: String) extends AnyVal

  lazy val appConfig = configured[AppConfig]

  implicit val url: MasterServer = appConfig.imageMaster

  /**
   * make a GET request to the MasterServer /image URL.
   * @see [[ProxyAction]]
   */
  private def getImageWS[A](request: Request[A])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").get
  }

  /**
   * make a POST request, obtaining the MasterServer reply from the /image URL.
   * @see [[ProxyAction]]
   */
  private def postImageToMaster(request: Request[JsValue])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").post(request.body)
  }

  /**
   * Handle the response of a [[ProxyAction]].
   */
  private def response = {
    responseFut: Future[Response] =>
      responseFut
        .map(response =>
          response.status match {
            case 200 => Ok(response.body)
            case _ => ServiceUnavailable(error(response.body))
          })
        .recover {
          case ex: Throwable => BadRequest(error(s"Error from server ${ex.getMessage}"))
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
  def getImage = ProxyAction(ws = getImageWS, reply = response)

  /**
   *  save the image tags to the MasterServer.
   */
  def postImage = ProxyAction(parse.json, ws = postImageToMaster, reply = response)

  def jsRoutes(varName: String = "jsRoutes") = Action { implicit request =>
    Ok(
      Routes.javascriptRouter(varName)(
        routes.javascript.Application.getImage,
        routes.javascript.Application.postImage)).as(JAVASCRIPT)
  }
}