package connectors.getAllChampions

import cats.effect.IO
import connectors.getAllChampions.models._
import connectors.models.{ErrorResponse, JsonErrorResponse, LolErrorResponse}
import io.circe
import io.circe.{DecodingFailure, Json, ParsingFailure}
import play.api.Logger
import play.api.cache.AsyncCacheApi

import scala.concurrent.ExecutionContext

class GetAllChampionsService(cache: AsyncCacheApi, getAllChampionsConnector: GetAllChampionsConnector) {

  val logger: Logger = Logger(this.getClass)

  def getAllChampions(implicit ec: ExecutionContext): IO[Either[ErrorResponse, Champions]] = {

    import cats.Eval
    import cats.implicits._
    import io.circe.generic.auto._

    def handleFailure(e: circe.Error): JsonErrorResponse = e match {
      case d: DecodingFailure =>
        logger.warn(s"Error decoding $LolErrorResponse ${d.message} ${d.history}")
        JsonErrorResponse(s"${d.message} ${d.history}")
      case p: ParsingFailure =>
        logger.warn(s"Error parsing $Json ${p.message}")
        JsonErrorResponse(p.message)
    }

    def parseJson(json: Json) = {
      json.as[Champions]
        .leftMap { res2 =>
          handleFailure(res2)
          json.as[LolErrorResponse]
            .fold(handleFailure, identity)
        }
    }

    IO.fromFuture(
      Eval.now(cache.getOrElseUpdate("championsList") {
        getAllChampionsConnector.getAllChampions
          .leftMap(handleFailure)
          .subflatMap(parseJson)
          .value
      })
    )
  }
}
