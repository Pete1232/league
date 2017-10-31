package connectors.getAllChampions

import cats.data.EitherT
import cats.implicits._
import connectors.getAllChampions.models.Champions
import connectors.models.{JsonErrorResponse, LolErrorResponse}
import io.circe
import io.circe.{DecodingFailure, Json, ParsingFailure}
import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import testutils.{MockCacheComponents, SharedTestFixtures}

import scala.concurrent.ExecutionContext

class GetAllChampionsServiceSpec extends PlaySpec with MockFactory with MockCacheComponents with SharedTestFixtures {

  val mockGetAllChampionsConnector: GetAllChampionsConnector = mock[GetAllChampionsConnector]

  val service = new GetAllChampionsService(defaultCacheApi, mockGetAllChampionsConnector)

  "Calling getAllChampions through the service" must {

    "download the champions if none are cached" in {

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT(champions.asJson)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(champions.model)
    }
    "cache the response in the default cache with the name championsList" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT(champions.asJson)

      service.getAllChampions.unsafeRunSync()

      await {
        defaultCacheApi.get[Either[circe.Error, Champions]]("championsList")
      }.get mustBe Right(champions.model)
    }
    "use the cached value if it is available" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT(champions.asJson) once()

      service.getAllChampions.unsafeRunSync()

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(champions.model)
    }
    s"return a $LolErrorResponse if a forbidden error response is received" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT(forbidden.asJson)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Left(forbidden.model)
    }
    s"return an $JsonErrorResponse if a decoding failure occurred" in {

      val error = DecodingFailure("Test decoding failure", List.empty)

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.leftT(error)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Left(JsonErrorResponse(s"${error.message} ${error.history}"))
    }
    s"return an $JsonErrorResponse if a parsing failure occurred" in {

      val error = ParsingFailure("Test parsing failure", new Exception("Test exception"))

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.leftT(error)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Left(JsonErrorResponse(error.message))
    }
    s"return a $JsonErrorResponse if the response could not be parsed as an expected response" in {

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT(
        Json.obj("badKey" -> Json.fromString("badValue"))
      )

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Left(JsonErrorResponse("Attempt to decode value on failed cursor List(DownField(status))"))
    }
  }
}
