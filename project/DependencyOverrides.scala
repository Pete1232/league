import sbt._

object DependencyOverrides {

  val akkaVersion = "2.5.4"

  def apply() = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )
}
