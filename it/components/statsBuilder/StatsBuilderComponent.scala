package components.statsBuilder

import play.api.http.{HeaderNames, Status}
import play.api.mvc.{Call, RequestHeader}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest}
import testutils.ComponentTestBase

class StatsBuilderComponent extends ComponentTestBase {

  lazy val statsBuilderCall: Call = routes.StatsBuilderController.displayChampions()

  "GET /champions/list when champion data was retrieved successfully" must {
    s"respond with ${Status.OK} status" in {
      withLeagueClient { client =>
        implicit val request: RequestHeader = FakeRequest().withHeaders(FakeHeaders(Seq(HeaderNames.HOST -> s"localhost:$port")))

        val response = await {
          client
            .url(statsBuilderCall.absoluteURL())
            .execute(statsBuilderCall.method)
        }

        response.status mustBe Status.OK
      }
    }
  }
}
