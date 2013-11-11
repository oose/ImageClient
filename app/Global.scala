import play.api._

import oose.play.config.Configuration
import util.AppConfig

object Global extends GlobalSettings with Configuration {

  override def onStart(app: play.api.Application) = {
    Logger.info("""
        Starting ImageClient
        """)
    configure {
      new AppConfig()
    }
  }
}