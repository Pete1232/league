package connectors.getAllChampions

import connectors._
import connectors.getAllChampions.models.Champions
import connectors.utilities.{ConnectorConfig, ConnectorLogging}
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GetAllChampionsConnector(connectorConfig: ConnectorConfig)
                              (implicit wsClient: WSClient, ec: ExecutionContext) extends ConnectorLogging {
  private val requestUrl = {
    import connectorConfig._
    s"$scheme://$host/lol/static-data/v3/champions"
  }

  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._

  def getAllChampions: Future[Either[Error, Champions]] = {
    val request =
      wsClient.url(requestUrl)
        .withHttpHeaders(RIOT_TOKEN_HEADER_KEY -> connectorConfig.apiKey)
        .withQueryStringParameters(TAGS.QUERY_KEY -> TAGS.STATS)
        .withMethod(HTTP.GET)

    request map { res =>
      decode[Champions](res.body)
    }
  }
}
