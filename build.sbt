import sbt._
import sbt.Keys._
import play.Project._

play.Project.playJavaSettings

name := """suggest_exercise_prototype"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.webjars"   %% "webjars-play"  % "2.2.0",
  // Downgrade to JQuery 1.8.3 so that integration tests with HtmlUnit work.
  "org.webjars" % "bootstrap" % "3.0.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "1.8.3",
  "net.sourceforge.owlapi" % "owlapi-distribution" % "4.0.1"
) 

resolvers ++= Seq(
  "webjars" at "http://webjars.github.com/m2"
)