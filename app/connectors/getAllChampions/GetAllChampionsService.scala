package connectors.getAllChampions

import cats.effect.IO
import connectors.getAllChampions.models.Champions
import io.circe
import play.api.cache.AsyncCacheApi

import scala.concurrent.ExecutionContext

class GetAllChampionsService(cache: AsyncCacheApi, getAllChampionsConnector: GetAllChampionsConnector) {

  def getAllChampions(implicit ec: ExecutionContext): IO[Either[circe.Error, Champions]] = {

    import cats.Eval
    import cats.implicits._
    import io.circe.generic.auto._

    IO.fromFuture(
      Eval.now(cache.getOrElseUpdate("championsList") {
        getAllChampionsConnector.getAllChampions
          .subflatMap(_.as[Champions].map(identity))
          .value
      })
    )
  }
}
