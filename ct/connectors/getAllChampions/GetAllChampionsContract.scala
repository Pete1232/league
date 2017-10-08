package connectors.getAllChampions

import connectors.getAllChampions.models.{Champion, ChampionStats}
import org.scalatestplus.play.{BaseOneAppPerSuite, PlaySpec}
import play.api.test.Helpers._
import testutils.TestApplicationFactory

class GetAllChampionsContract extends PlaySpec with BaseOneAppPerSuite with TestApplicationFactory {

  lazy val connector: GetAllChampionsConnector = components.getAllChampionsConnector

  "The getAllChampions method" must {
    "return a list of all champions without error" in {
      val result = await(connector.getAllChampions)

      result.isRight mustBe true

      result.right.get.data("Annie") mustBe Champion("Annie", "the Dark Child", ChampionStats(511.68, 76))
    }
  }
}
