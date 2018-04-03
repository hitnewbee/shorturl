name := """shorturl"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.11"

Common.settings
libraryDependencies ++=Common.play_dependencies

lazy val root = (project in file(".")).enablePlugins(PlayJava)
