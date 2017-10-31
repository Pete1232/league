package connectors.getAllChampions

import cats.effect.IO
import connectors.getAllChampions.models._
import connectors.models.{ErrorResponse, JsonErrorResponse, LolErrorResponse}
import io.circe.{DecodingFailure, ParsingFailure}
import play.api.cache.AsyncCacheApi

import scala.concurrent.ExecutionContext

class GetAllChampionsService(cache: AsyncCacheApi, getAllChampionsConnector: GetAllChampionsConnector) {

  def getAllChampions(implicit ec: ExecutionContext): IO[Either[ErrorResponse, Champions]] = {

    import cats.Eval
    import cats.implicits._
    import io.circe.generic.auto._

    IO.fromFuture(
      Eval.now(cache.getOrElseUpdate("championsList") {
        getAllChampionsConnector.getAllChampions
          .leftMap {
            case d: DecodingFailure => JsonErrorResponse(d.message)
            case p: ParsingFailure => JsonErrorResponse(p.message)
          }
          .subflatMap(res => res.as[Champions]
            .leftMap(_ => res.as[LolErrorResponse]
              .fold(d => JsonErrorResponse(s"${d.message} ${d.history}"), identity)
            )
            .map(identity)
          )
          .value
      })
    )
  }
}
