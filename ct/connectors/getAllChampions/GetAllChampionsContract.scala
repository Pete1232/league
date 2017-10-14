package connectors.getAllChampions

import connectors.getAllChampions.models.{Champion, ChampionStats}
import org.scalatestplus.play.{BaseOneAppPerSuite, PlaySpec}
import play.api.test.Helpers._
import testutils.CTTestApplicationFactory

class GetAllChampionsContract extends PlaySpec with BaseOneAppPerSuite with CTTestApplicationFactory {

  lazy val connector: GetAllChampionsConnector = components.getAllChampionsConnector

  "The successful response from the get all champions API" must {

    val result = await(connector.getAllChampions)

    "return a list of all champions" in {
      result.right.map(_.data("Annie")) mustBe Right(Champion("Annie", "the Dark Child", ChampionStats(511.68, 76)))
    }
  }
}
