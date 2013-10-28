package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.json.JsValue
import play.api.libs.ws.Response
import common.config.Configured
import play.api.Play
import common.config.Configuration
import util.AppConfig

object Application extends Controller  with Configured {

  implicit class MasterServer(val url: String) extends AnyVal

  lazy val appConfig = configured[AppConfig]

  implicit val url: MasterServer = appConfig.imageMaster

  class ProxyAction[A](
    ws: Request[A] => Future[Response], bodyParser: BodyParser[A] = parse.anyContent)(reply: Future[Response] => Future[SimpleResult])
    extends Action[A] {
    def apply(request: Request[A]): Future[SimpleResult] = {
      (reply compose ws)(request)
    }

    def parser = bodyParser
  }

  object ProxyAction {
    def apply[A](ws: Request[A] => Future[Response], bodyParser: BodyParser[A] = parse.anyContent)(reply: Future[Response] => Future[SimpleResult]) = new ProxyAction(ws, bodyParser)(reply)
  }

  def getImage[A](request: Request[A])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").get
  }

  def postImage(request: Request[JsValue])(implicit masterServer: MasterServer) = {
    WS.url(masterServer.url + "/image").post(request.body)
  }

  def response = {
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

  def index = Action {
    Ok(views.html.index())
  }

  def image = ProxyAction(getImage)(response)

  def saveData = ProxyAction(postImage, parse.json)(response)
}