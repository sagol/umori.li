import sbt._
import Keys._
//import PlayProject._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "funr"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "nu.validator.htmlparser" % "htmlparser" % "1.4",
      "org.jsoup" % "jsoup" % "1.7.1"
  )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )
}
