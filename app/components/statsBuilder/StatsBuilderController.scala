package components.statsBuilder

import _root_.views.html.errorModel
import connectors.getAllChampions.GetAllChampionsService
import connectors.getAllChampions.models.Champions
import connectors.models.{ErrorResponse, LolErrorResponse}
import connectors.utilities.CirceWriteable
import controllers.AssetsFinder
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc._

import scala.concurrent.ExecutionContext

class StatsBuilderController(getAllChampionsService: GetAllChampionsService, controllerComponents: ControllerComponents)
                            (implicit ec: ExecutionContext, assetsFinder: AssetsFinder)
  extends AbstractController(controllerComponents) with CirceWriteable {

  val logger = Logger(this.getClass)

  private def handleExpected(out: Either[ErrorResponse, Champions]): Messages => Result = {
    (_: Messages) =>
      out match {
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
      }
  }

  private def handleUnexpected(out: Either[Throwable, Messages => Result]): Messages => Result = {
    out match {
      case Left(e) =>
        logger.warn(e.getMessage)
        (messages: Messages) => ServiceUnavailable(errorModel(messages("errors.generic")))
      case Right(res) => res
    }
  }

  private def runWithMessages(resultWithMessages: Messages => Result): Action[AnyContent] = {
    Action { request =>
      val messages = messagesApi.preferred(request)
      resultWithMessages(messages)
    }
  }

  def displayChampions: Action[AnyContent] = {

    getAllChampionsService.getAllChampions
      .map(handleExpected)
      .attempt
      .map(handleUnexpected _ andThen runWithMessages)
      .unsafeRunSync()
  }
}
