package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.utilities.CirceWriteable
import play.api.Logger
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

class StatsBuilderController(getAllChampionsConnector: GetAllChampionsConnector, controllerComponents: ControllerComponents)
                            (implicit ec: ExecutionContext)
  extends AbstractController(controllerComponents) with CirceWriteable {

  val logger = Logger(this.getClass)

  def displayChampions: Action[AnyContent] = Action.async {

    logger.debug("Request displayChampions started")

    getAllChampionsConnector.getAllChampions.map {
      case Right(champions) =>
        logger.debug(s"Received ${champions.toString.take(50)} from league servers")
        import io.circe.generic.auto._
        import io.circe.syntax._
        Ok(champions.asJson)
    }
  }
}
