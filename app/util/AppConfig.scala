package util

import play.api._

class AppConfig {

  val imageMaster: String = {
    Play.current.configuration.getString("imageclient.imagemaster")
      .getOrElse("http://localhost:10000")
  }
}