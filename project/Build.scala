import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "ImageClient"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.webjars" %% "webjars-play" % "2.2.0",
    "org.webjars" % "angularjs" % "1.2.0-rc.3",
    "org.webjars" % "bootstrap" % "2.3.2")

  lazy val imageCommon = RootProject(file("../ImageCommon/"))

  val main = play.Project(appName, appVersion, appDependencies).settings( 
      // Add your own project settings here      
  ).aggregate(imageCommon).dependsOn(imageCommon)
}
