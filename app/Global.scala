import play.api.GlobalSettings
import common.config.Configuration
import play.api.Logger
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