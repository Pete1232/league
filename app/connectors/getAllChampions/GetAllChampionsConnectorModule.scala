package connectors.getAllChampions

import connectors.utilities.ConnectorConfig
import play.api.{Configuration, Mode}
import play.api.cache.AsyncCacheApi
import play.api.libs.ws.WSClient

trait GetAllChampionsConnectorModule {

  import com.softwaremill.macwire._

  def mode: Mode

  def configuration: Configuration

  def defaultCacheApi: AsyncCacheApi

  implicit def wsClient: WSClient

  lazy val connectorConfig: ConnectorConfig = wire[ConnectorConfig]

  lazy val getAllChampionsConnector: GetAllChampionsConnector = wire[GetAllChampionsConnector]

  lazy val getAllChampionsService: GetAllChampionsService = wire[GetAllChampionsService]
}
