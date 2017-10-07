package connector

import connectors.getAllChampions.GetAllChampionsConnector
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._

class GetAllChampionsConnectorIT extends PlaySpec with GuiceOneAppPerSuite {

  lazy val connector: GetAllChampionsConnector = app.injector.instanceOf[GetAllChampionsConnector]

  "getAllChampions" must {
    "return a list of all champions" in {
      await(connector.getAllChampions) mustBe ""
    }
  }
}
