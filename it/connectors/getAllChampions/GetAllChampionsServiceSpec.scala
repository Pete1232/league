package connectors.getAllChampions

import connectors.getAllChampions.models.{Champion, ChampionStats, Champions}
import io.circe
import org.scalamock.scalatest.MockFactory
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import testutils.MockCacheComponents

import scala.concurrent.{ExecutionContext, Future}

class GetAllChampionsServiceSpec extends PlaySpec with MockFactory with MockCacheComponents {

  val mockGetAllChampionsConnector: GetAllChampionsConnector = mock[GetAllChampionsConnector]

  val service = new GetAllChampionsService(defaultCacheApi, mockGetAllChampionsConnector)

  "Calling getAllChampions through the service" must {

    val mockedChampionDetails = Champions(
      "champion",
      "7.20.2",
      Map(
        "Annie" -> Champion("Annie", "the Dark Child", ChampionStats(511.68, 76))
      )
    )

    "download the champions if none are cached" in {

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning Future.successful(Right(mockedChampionDetails))

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(mockedChampionDetails)
    }
    "cache the response in the default cache with the name championsList" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning Future.successful(Right(mockedChampionDetails))

      service.getAllChampions.unsafeRunSync()

      await {
        defaultCacheApi.get[Either[circe.Error, Champions]]("championsList")
      }.get mustBe Right(mockedChampionDetails)
    }
    "use the cached value if it is available" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning Future.successful(Right(mockedChampionDetails)) once()

      service.getAllChampions.unsafeRunSync()

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(mockedChampionDetails)
    }
  }
}
