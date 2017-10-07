import play.sbt.PlayImport._
import sbt._

object Dependencies {

  private val circe: Seq[ModuleID] = {
    val circeVersion = "0.8.0"
    Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  }

  val compile: Seq[ModuleID] = Seq(
    ws,
    guice //TODO remove for macwire
  ) ++ circe

  val test: Seq[ModuleID] = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2",
    "com.github.tomakehurst" % "wiremock" % "2.8.0"
  ).map(_ % "test,it")

  def apply(): Seq[ModuleID] = compile ++ test
}
