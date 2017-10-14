package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.getAllChampions.models.{Champion, ChampionStats, Champions}
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import testutils.ControllerTestBase

import scala.concurrent.Future

class StatsBuilderControllerSpec extends ControllerTestBase {

  val mockedChampionDetails = Champions(
    "champion",
    "7.20.2",
    Map(
      "Annie" -> Champion("Annie", "the Dark Child", ChampionStats(511.68, 76))
    )
  )

  lazy val mockChampionsConnector: GetAllChampionsConnector = mock[GetAllChampionsConnector]

  lazy val controller: StatsBuilderController = new StatsBuilderController(mockChampionsConnector, mockControllerComponents)

  "Calling displayChampions when champion data was retrieved successfully" must {

    mockChampionsConnector.getAllChampions _ expects() returning Future.successful(Right(mockedChampionDetails))

    val request = FakeRequest()

    lazy val result: Future[Result] = controller.displayChampions(request)

    s"return ${Status.OK} status" in {
      status(result) mustBe Status.OK
    }
    "show the JSON returned by the API" in {
      contentAsString(result) must include(mockedChampionDetails.`type`) //TODO not a very good test, but it will be changing anyway
    }
  }
}
