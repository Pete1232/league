import sbt._

object Dependencies {

  val compile: Seq[ModuleID] = Seq(
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"
  ).map(_ % "test,it")

  def apply(): Seq[ModuleID] = compile ++ test
}
