import play.sbt.PlayImport._
import sbt._
import Keys._
import play.sbt.routes.RoutesKeys._


object Common{
  val settings: Seq[Setting[_]] = Seq(
    organization :=org,
    version :=ver,
    scalaVersion :="2.11.11",
    routesGenerator :=InjectedRoutesGenerator
  )
  private val ver = Option(System.getenv("system_version")).getOrElse("1.0-SNAPSHOT")
  private val org = "com.cloud"

  def module(name:String)= org %%name %ver

  private val PLAY_VERSION = "2.5.18"
  private val HIBERNATE_VERSION = "5.2.5.Final"
  val akka_actor = "com.typesafe.akka" %"akka-actor_2.11"%"2.3.13"
  val slf4j = "org.slf4j" %"slf4j-api" %"1.7.12"
  val javax_inject ="javax.inject" %"javax.inject"%"1"
  val guice ="com.google.inject"%"guice"%"4.0"
  val mysql = "mysql" %"mysql-connector-java" %"5.1.43"
  val hibernateJPA = "org.hibernate" %"hibernate-entitymanager"%HIBERNATE_VERSION
  val hibernateJava8 = "org.hibernate" %"hibernate-java8" %HIBERNATE_VERSION
  val play = ("com.typesafe.play"%"play_2.11"%PLAY_VERSION)
      .exclude("org.scala-lang.modules","scala-java8-compat_2.11")
  val base_dependencies = Seq(akka_actor,slf4j)
  val play_dependencies = Seq(jdbc,cache,ws,javaJpa,javaCore,javaJdbc,json,javaWs,
    evolutions,specs2%Test,filters,hibernateJava8,hibernateJPA)
  val inject_dependencies = Seq(javax_inject,guice,play)
  val scala_java8_compat = Seq("org.scala-lang.modules" %"scala-java8-compat_2.11"%"0.7.0")
}