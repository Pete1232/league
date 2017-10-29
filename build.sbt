val APP_NAME = "league"
val SCALA_VERSION = "2.12.3"

lazy val ContractTest = config("ct") extend Test

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
  .settings(
    libraryDependencies ++= Dependencies(),
    dependencyOverrides ++= DependencyOverrides()
  )
  .settings(
    mainClass in assembly := Some("play.core.server.ProdServerStart"),
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value),
    assemblyMergeStrategy in assembly := {
      case PathList(xs@_*) if xs.last endsWith ".conf" => MergeStrategy.concat
      case x => (assemblyMergeStrategy in assembly).value(x)
    }
  )

lazy val projectItSettings = Seq(
  configuration := configuration.value extend Test,
  scalaSource := baseDirectory.value / "it",
  resourceDirectory := baseDirectory.value / "it" / "resources",
  parallelExecution := false
)

lazy val projectContractSettings = Seq(
  scalaSource := baseDirectory.value / "ct",
  resourceDirectory := baseDirectory.value / "ct" / "resources",
  parallelExecution := false
)
