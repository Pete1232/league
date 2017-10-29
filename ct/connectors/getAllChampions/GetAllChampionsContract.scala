package connectors.getAllChampions

import connectors.getAllChampions.models._
import connectors.utilities.ConnectorConfig
import org.scalatestplus.play.{BaseOneAppPerSuite, PlaySpec}
import play.api.Configuration
import play.api.test.Helpers._
import testutils.{CTTestApplicationFactory, SharedTestFixtures}

class GetAllChampionsContract extends PlaySpec with BaseOneAppPerSuite with CTTestApplicationFactory with SharedTestFixtures {

  lazy val defaultConnector: GetAllChampionsConnector = components.getAllChampionsConnector

  lazy val connectorWithInvalidApiKey: GetAllChampionsConnector = new GetAllChampionsConnector(
    new ConnectorConfig(
      components.configuration ++
      Configuration("riotgames.developer.apiKey" -> "notARealKey"),
      components.mode
    )
  )(components.wsClient)

  "The successful response from the get all champions API" must {

    import io.circe.generic.auto._

    "return a list of all champions if data was returned successfully" in {

      val result = await(defaultConnector.getAllChampions.value)
        .map(_.as[Champions])
        .right.get

      result.right.map(_.data("Annie")) mustBe Right(champions2.model.data("Annie"))
    }

    "return a parsed forbidden exception if the API key was invalid" in {

      val result = await(connectorWithInvalidApiKey.getAllChampions.value)
        .map(_.as[LolErrorResponse])
        .right.get

      result.right.get mustBe forbidden.model
    }
  }
}
