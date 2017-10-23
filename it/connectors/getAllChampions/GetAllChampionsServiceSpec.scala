package connectors.getAllChampions

import cats.data.EitherT
import cats.implicits._
import connectors.getAllChampions.models.{Champion, ChampionStats, Champions}
import io.circe
import io.circe.{Error, Json}
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

    val mockedChampionDetailsAsJson: Json = {
      import io.circe.generic.auto._
      import io.circe.syntax._

      mockedChampionDetails.asJson
    }

    "download the champions if none are cached" in {

      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](mockedChampionDetailsAsJson)

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(mockedChampionDetails)
    }
    "cache the response in the default cache with the name championsList" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](mockedChampionDetailsAsJson)

      service.getAllChampions.unsafeRunSync()

      await {
        defaultCacheApi.get[Either[circe.Error, Champions]]("championsList")
      }.get mustBe Right(mockedChampionDetails)
    }
    "use the cached value if it is available" in {
      (mockGetAllChampionsConnector.getAllChampions(_: ExecutionContext)) expects * returning EitherT.rightT[Future, Error](mockedChampionDetailsAsJson) once()

      service.getAllChampions.unsafeRunSync()

      val result = service.getAllChampions.unsafeRunSync()

      result mustBe Right(mockedChampionDetails)
    }
  }
}
