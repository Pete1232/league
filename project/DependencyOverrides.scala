import sbt._

object DependencyOverrides {

  val akkaVersion: String = "2.5.4"

  def apply(): Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.typelevel" %% "cats-core" % "1.0.0-MF"
  )
}
