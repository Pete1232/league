package connectors.getAllChampions

import connectors.config.ConnectorConfig
import connectors.getAllChampions.models.Champions
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

class GetAllChampionsConnector(connectorConfig: ConnectorConfig)
                              (implicit wsClient: WSClient, ec: ExecutionContext) {

  private val requestUrl = {
    import connectorConfig._
    s"$scheme://$host/lol/static-data/v3/champions"
  }

  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._

  def getAllChampions: Future[Either[Error, Champions]] =
    wsClient.url(requestUrl)
      .withHttpHeaders("X-Riot-Token" -> connectorConfig.apiKey)
      .get()
      .map { res =>
        println(res.headers)
        decode[Champions](res.body)
      }
}
