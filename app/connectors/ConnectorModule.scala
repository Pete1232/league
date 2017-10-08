package connectors

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.utilities.ConnectorConfig
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

trait ConnectorModule {

  import com.softwaremill.macwire._

  def configuration: Configuration

  implicit def wsClient: WSClient

  implicit def executionContext: ExecutionContext

  lazy val connectorConfig: ConnectorConfig = wire[ConnectorConfig]

  lazy val getAllChampionsConnector: GetAllChampionsConnector = wire[GetAllChampionsConnector]
}
