name := "umorili"

version := "2.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.webjars" % "requirejs" % "2.1.11-1",
//  "com.typesafe.akka" % "akka-actor" % "2.3.4",
  "nu.validator.htmlparser" % "htmlparser" % "1.4",
  "org.jsoup" % "jsoup" % "1.7.1",
  "com.newrelic.agent.java" % "newrelic-agent" % "2.21.6",
  "be.cafeba" %% "play-cors" % "1.0"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(SbtWeb)

