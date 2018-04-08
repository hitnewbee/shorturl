name := """shorturl"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.11"

Common.settings
libraryDependencies ++=Common.play_dependencies
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % "test"
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % "test"
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

lazy val root = (project in file(".")).enablePlugins(PlayJava)
