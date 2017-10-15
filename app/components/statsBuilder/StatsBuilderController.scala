package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.utilities.CirceWriteable
import controllers.AssetsFinder
import play.api.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

class StatsBuilderController(getAllChampionsConnector: GetAllChampionsConnector, controllerComponents: ControllerComponents)
                            (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends AbstractController(controllerComponents) with CirceWriteable {

  val logger = Logger(this.getClass)

  def displayChampions: Action[AnyContent] = Action.async {

    logger.debug("Request displayChampions started")

    getAllChampionsConnector.getAllChampions.map {
      case Right(champions) =>
        logger.debug(s"Received ${champions.toString.take(50)} from league servers")
        Ok(components.statsBuilder.views.html.championsList(champions))
    }
  }
}
