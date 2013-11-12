name := "ImageClient"

version := "1.0-SNAPSHOT"

resolvers += "oose (snapshots)" at "http://oose.github.io/m2/snapshots"

libraryDependencies ++= Seq(
    "org.webjars" %% "webjars-play" % "2.2.0",
    "org.webjars" % "requirejs" % "2.1.8",
    "org.webjars" % "angularjs" % "1.2.0-rc.3",
    "org.webjars" % "bootstrap" % "2.3.2",
    "oose.play" %% "config" % "1.0-SNAPSHOT",
    "oose.play" %% "actions" % "1.0-SNAPSHOT",
    "oose.play" %% "jsrouter" % "1.0-SNAPSHOT",
    "com.typesafe.akka" %% "akka-testkit" % "2.2.0" % "test")
    
play.Project.playScalaSettings


val ImageClient = project.in(file("."))
   
