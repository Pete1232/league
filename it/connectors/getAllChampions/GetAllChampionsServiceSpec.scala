package connectors.getAllChampions

import cats.data.EitherT
import cats.implicits._
import connectors.getAllChampions.models.Champions
import io.circe
import io.circe.Error
import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import testutils.{MockCacheComponents, SharedTestFixtures}

import scala.concurrent.{ExecutionContext, Future}

class GetAllChampionsServiceSpec extends PlaySpec with MockFactory with MockCacheComponents with SharedTestFixtures {

  val mockGetAllChampionsConnector: GetAllChampionsConnector = mock[GetAllChampionsConnector]

  val service = new GetAllChampionsService(defaultCacheApi, mockGetAllChampionsConnector)

  "Calling getAllChampions through the service" must {

    "download the champions if none are cached" in {

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](champions.asJson)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(champions.model)
    }
    "cache the response in the default cache with the name championsList" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](champions.asJson)

      service.getAllChampions.unsafeRunSync()

      await {
        defaultCacheApi.get[Either[circe.Error, Champions]]("championsList")
      }.get mustBe Right(champions.model)
    }
    "use the cached value if it is available" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](champions.asJson) once()

      service.getAllChampions.unsafeRunSync()

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(champions.model)
    }
  }
}
