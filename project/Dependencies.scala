import play.sbt.PlayImport._
import sbt._

object Dependencies {

  private val circe: Seq[ModuleID] = {
    val version = "0.8.0"
    Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % version)
  }

  private val macwire = {
    val version = "2.3.0"
    Seq(
      "com.softwaremill.macwire" %% "util",
      "com.softwaremill.macwire" %% "macros"
    ).map(_ % version)
  }

  private val compile: Seq[ModuleID] = Seq(
    ws
  ) ++ circe

  private val provided: Seq[ModuleID] = Seq(
  ) ++ macwire
    .map(_ % "provided")

  private val test: Seq[ModuleID] = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "com.github.tomakehurst" % "wiremock" % "2.8.0"
  ).map(_ % "test,it")

  def apply(): Seq[ModuleID] = compile ++ provided ++ test
}
