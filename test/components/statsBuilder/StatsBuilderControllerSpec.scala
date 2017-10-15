package components.statsBuilder

import connectors.getAllChampions.GetAllChampionsConnector
import connectors.getAllChampions.models.{Champion, ChampionStats, Champions}
import controllers.AssetsFinder
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

  implicit lazy val assetsFinder: AssetsFinder = mock[AssetsFinder]

  lazy val mockChampionsConnector: GetAllChampionsConnector = mock[GetAllChampionsConnector]

  lazy val controller: StatsBuilderController = new StatsBuilderController(mockChampionsConnector, mockControllerComponents)

  "Calling displayChampions when champion data was retrieved successfully" must {

    assetsFinder.path _ expects "lib/bootstrap/css/bootstrap.css" returning ""

    mockChampionsConnector.getAllChampions _ expects() returning Future.successful(Right(mockedChampionDetails))

    val request = FakeRequest()

    lazy val result: Future[Result] = controller.displayChampions(request)

    lazy val resultAsString: String = contentAsString(result)

    s"return ${Status.OK} status" in {
      status(result) mustBe Status.OK
    }
    "render the champions list page" in {
      resultAsString must include("""<title>Champion List</title>""")
    }
  }
}
