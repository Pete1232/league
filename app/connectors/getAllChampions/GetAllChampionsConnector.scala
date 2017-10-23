package connectors.getAllChampions

import cats.data.EitherT
import connectors._
import connectors.utilities.{ConnectorConfig, ConnectorLogging}
import io.circe.{Error, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GetAllChampionsConnector(connectorConfig: ConnectorConfig)
                              (implicit wsClient: WSClient) extends ConnectorLogging {
  private lazy val requestUrl = {
    import connectorConfig._
    s"$scheme://$host/lol/static-data/v3/champions"
  }

  def getAllChampions(implicit ec: ExecutionContext): EitherT[Future, Error, Json] = {
    val res = wsClient.url(requestUrl)
      .withHttpHeaders(RIOT_TOKEN_HEADER_KEY -> connectorConfig.apiKey)
      .withQueryStringParameters(TAGS.QUERY_KEY -> TAGS.STATS)
      .withMethod(HTTP.GET)

    import io.circe._
    import io.circe.parser._

    EitherT(
      res.map { resp =>
        decode[Json](resp.body)
      })
  }
}
