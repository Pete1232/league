package connectors

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.utilities.ConnectorConfig
import play.api.libs.ws.WSClient
import play.api.{Configuration, Mode}

import scala.concurrent.ExecutionContext

trait ConnectorModule {

  import com.softwaremill.macwire._

  def configuration: Configuration

  implicit val wsClient: WSClient

  implicit val executionContext: ExecutionContext

  val mode: Mode

  val connectorConfig: ConnectorConfig = wire[ConnectorConfig]

  lazy val getAllChampionsConnector: GetAllChampionsConnector = wire[GetAllChampionsConnector]
}
