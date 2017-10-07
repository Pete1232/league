val APP_NAME = "league"
val SCALA_VERSION = "2.12.3"

lazy val league = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(
    name := APP_NAME,
    scalaVersion := SCALA_VERSION
  )
  .settings(
    inConfig(IntegrationTest)(
      Defaults.itSettings ++
        projectItSettings
    ): _*
  )
  .settings(libraryDependencies ++= Dependencies())

lazy val projectItSettings = Seq(
  scalaSource := baseDirectory.value / "it",
  resourceDirectory := baseDirectory.value / "it" / "resources",
  parallelExecution := false
)
