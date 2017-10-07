package connectors.getAllChampions

import javax.inject.{Inject, Singleton}

import connectors.config.ConnectorConfig
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GetAllChampionsConnector @Inject()(connectorConfig: ConnectorConfig)
                                        (implicit wsClient: WSClient, ec: ExecutionContext) {

  private val requestUrl = {
    import connectorConfig._
    s"$scheme://$host/lol/platform/v3/champions"
  }

  def getAllChampions: Future[String] =
    wsClient.url(requestUrl)
      .withHttpHeaders("X-Riot-Token" -> connectorConfig.apiKey)
      .get()
      .map(_.body)
}
