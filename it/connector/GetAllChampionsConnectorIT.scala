package connector

import connectors.getAllChampions.GetAllChampionsConnector
import org.scalatestplus.play.{BaseOneAppPerSuite, PlaySpec}
import play.api.test.Helpers._
import testutils.TestApplicationFactory

class GetAllChampionsConnectorIT extends PlaySpec with BaseOneAppPerSuite with TestApplicationFactory {

  lazy val connector: GetAllChampionsConnector = components.getAllChampionsConnector

  "getAllChampions" must {
    "return a list of all champions without error" in {
      val result = await(connector.getAllChampions)

      result.isRight mustBe true
    }
  }
}
