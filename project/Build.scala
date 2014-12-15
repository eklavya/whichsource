import sbt._
import Keys._
import play.Play.autoImport._
import PlayKeys._

object ApplicationBuild extends Build {

  val appName         = "whichsource"
  val appVersion      = "1.0-SNAPSHOT"
  val scalaVersion    = "2.11.4"
  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
      "org.scalariform" % "scalariform_2.10" % "0.1.4",
      "com.typesafe.akka" %% "akka-actor" % "2.3.6",
      "com.typesafe.akka" %% "akka-testkit" % "2.3.6",
      "org.eclipse.jdt" % "org.eclipse.jdt.core" % "3.7.1",
      "com.typesafe.slick" %% "slick" % "2.1.0",
      "com.typesafe.play" %% "play-slick" % "0.8.1"
    )

val main = Project(appName, file(".")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= appDependencies)
}
