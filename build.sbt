val APP_NAME = "league"
val SCALA_VERSION = "2.12.3"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(
    name := APP_NAME,
    scalaVersion := SCALA_VERSION
  )
  .settings(
    Defaults.itSettings,
    sourceDirectory in IntegrationTest := baseDirectory.value / "it",
    unmanagedSourceDirectories in IntegrationTest += (sourceDirectory in IntegrationTest).value
  )
  .settings(libraryDependencies ++= Dependencies())
