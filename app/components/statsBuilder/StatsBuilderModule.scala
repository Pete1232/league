package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnector
import controllers.AssetsFinder
import play.api.Configuration
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

trait StatsBuilderModule {

  import com.softwaremill.macwire._

  def configuration: Configuration

  val getAllChampionsConnector: GetAllChampionsConnector

  val controllerComponents: ControllerComponents

  implicit val executionContext: ExecutionContext

  implicit val implicitAssetsFinder: AssetsFinder

  lazy val statsBuilderController: StatsBuilderController = wire[StatsBuilderController]
}
