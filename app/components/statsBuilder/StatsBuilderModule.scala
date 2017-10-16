package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnectorModule
import controllers.AssetsFinder
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

trait StatsBuilderModule extends GetAllChampionsConnectorModule {

  import com.softwaremill.macwire._

  def controllerComponents: ControllerComponents

  implicit def executionContext: ExecutionContext

  implicit def assetsFinder: AssetsFinder

  lazy val statsBuilderController: StatsBuilderController = wire[StatsBuilderController]
}
