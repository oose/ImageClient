name := "ImageClient"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
    "org.webjars" %% "webjars-play" % "2.2.0",
    "org.webjars" % "angularjs" % "1.2.0-rc.3",
    "org.webjars" % "requirejs" % "2.1,8",
    "org.webjars" % "bootstrap" % "2.3.2",
    "com.typesafe.akka" %% "akka-testkit" % "2.2.0" % "test")
    
play.Project.playScalaSettings

lazy val ImageCommon = RootProject(file("../ImageCommon/"))

val ImageClient = project.in(file("."))
    .aggregate(ImageCommon).dependsOn(ImageCommon)
