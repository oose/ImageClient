package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WS
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def image = Action.async {
    val response = WS.url("http://localhost:10000/image").get
    response.map(response =>
      response.status match {
        case 200 => Ok(response.body)
        case _ => ServiceUnavailable(response.body)
      }).recover {
      case ex: Throwable => BadRequest
    }
  }

  def saveData = Action.async(parse.json) {
    request =>
      val response = WS.url("http://localhost:10000/image").post(request.body)
      response.map(r =>
        r.status match {
          case 200 => Ok(r.body)
          case _ => BadRequest(r.body)
        }).recover {
        case ex: Exception => ServiceUnavailable(ex.getMessage)
      }
  }
}