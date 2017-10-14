val APP_NAME = "league"
val SCALA_VERSION = "2.12.3"

lazy val ContractTest = config("ct") extend Test // integration tests not using caching and affected by rate-limiting

lazy val league = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest, ContractTest)
  .settings(
    name := APP_NAME,
    scalaVersion := SCALA_VERSION
  )
  .settings(
    inConfig(IntegrationTest)(
      Defaults.testSettings ++
        projectItSettings
    ): _*
  )
  .settings(
    inConfig(ContractTest)(
      Defaults.testSettings ++
        projectContractSettings
    ): _*
  )
  .settings(libraryDependencies ++= Dependencies())

lazy val projectItSettings = Seq(
  scalaSource := baseDirectory.value / "it",
  resourceDirectory := baseDirectory.value / "it" / "resources",
  parallelExecution := false
)

lazy val projectContractSettings = Seq(
  scalaSource := baseDirectory.value / "ct",
  resourceDirectory := baseDirectory.value / "ct" / "resources",
  parallelExecution := false
)
