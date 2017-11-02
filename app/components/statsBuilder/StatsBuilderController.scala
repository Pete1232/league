package components.statsBuilder

import _root_.views.html.errorModel
import connectors.getAllChampions.GetAllChampionsService
import connectors.models.{ErrorResponse, LolErrorResponse}
import connectors.utilities.CirceWriteable
import controllers.AssetsFinder
import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext

class StatsBuilderController(getAllChampionsService: GetAllChampionsService, controllerComponents: ControllerComponents)
                            (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends AbstractController(controllerComponents) with CirceWriteable {

  val logger = Logger(this.getClass)

  def displayChampions: Action[AnyContent] = {

    logger.debug("Request displayChampions started")

    getAllChampionsService.getAllChampions.map {
      case Right(champions) =>
        logger.debug(s"Received ${champions.toString.take(50)} from league servers")
        Ok(components.statsBuilder.views.html.championsList(champions))
      case Left(e) =>
        e match {
          case LolErrorResponse(s) =>
            Status(s.status_code)(errorModel(s.message))
          case e: ErrorResponse =>
            ServiceUnavailable(errorModel(e.message))
        }
    }.map(Action(_)).unsafeRunSync()
  }
}
