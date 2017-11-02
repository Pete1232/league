package components.statsBuilder

import cats.effect.IO
import connectors.getAllChampions.GetAllChampionsService
import connectors.models.{LolErrorResponse, LolErrorStatus}
import controllers.AssetsFinder
import org.scalacheck.Gen
import play.api.http.Status
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import testutils.ControllerTestBase

import scala.concurrent.{ExecutionContext, Future}

class StatsBuilderControllerSpec extends ControllerTestBase {

  implicit lazy val assetsFinder: AssetsFinder = stub[AssetsFinder]

  assetsFinder.path _ when "lib/bootstrap/css/bootstrap.css" returns ""

  lazy val mockChampionsService: GetAllChampionsService = mock[GetAllChampionsService]

  lazy val controller: StatsBuilderController = new StatsBuilderController(mockChampionsService, mockControllerComponents)

  "Calling displayChampions when champion data was retrieved successfully" must {

    val request = FakeRequest()

    lazy val result: Future[Result] = controller.displayChampions(request)

    lazy val resultAsString: String = contentAsString(result)

    s"return ${Status.OK} status" in {

      (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Right(champions.model))

      status(result) mustBe Status.OK
    }
    "render the champions list page" in {

      (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Right(champions.model))

      resultAsString mustBe components.statsBuilder.views.html.championsList(champions.model).body
    }
  }

  s"Calling displayChampions when an $LolErrorResponse was returned" must {

    val request = FakeRequest()

    lazy val result: Future[Result] = controller.displayChampions(request)

    s"return status code $FORBIDDEN for a forbidden error" in {
      (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Left(forbidden.model))

      status(result) mustBe Status.FORBIDDEN
    }
    "return the forbidden error message for a forbidden error" in {
      (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Left(forbidden.model))

      contentAsString(result) mustBe _root_.views.html.errorModel(forbidden.model.message).body
    }
    "return the status code from the error" in {

      val statusCodeGen = Gen.choose(100, 599)

      forAll(statusCodeGen) { statusCode: Int =>

        (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Left(LolErrorResponse(LolErrorStatus("", statusCode))))

        lazy val result: Future[Result] = controller.displayChampions(request)

        status(result) mustBe statusCode
      }
    }
    "return the error message in the body of the request" in {

      val messageBodyGen = Gen.alphaNumStr

      forAll(messageBodyGen) { message: String =>

        (mockChampionsService.getAllChampions(_: ExecutionContext)) expects * returning IO(Left(LolErrorResponse(LolErrorStatus(message, SERVICE_UNAVAILABLE))))

        lazy val result: Future[Result] = controller.displayChampions(request)

        contentAsString(result) mustBe _root_.views.html.errorModel(message).body
      }
    }
  }
}
