package components.statsBuilder

import _root_.views.html.errorModel
import connectors.getAllChampions.GetAllChampionsService
import connectors.models.LolErrorResponse
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
      case Left(LolErrorResponse(statusModel)) =>
        Status(statusModel.status_code)(errorModel(statusModel.message))
    }.map(res =>
      Action(res)
    ).unsafeRunSync()
  }
}
