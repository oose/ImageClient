package util

import play.api._

class AppConfig {
  
   val imageMaster = {
      Play.current.configuration.getString("imageclient.imagemaster") match {
      case Some(url) => url
      case None => "http://localhost:10000"
    }
   }
   
   
}