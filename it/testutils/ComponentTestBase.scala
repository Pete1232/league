package testutils

import org.scalatestplus.play.{BaseOneServerPerTest, PlaySpec}
import play.api.libs.ws.WSClient
import play.api.routing.{Router, SimpleRouter}
import play.api.{BuiltInComponentsFromContext, Mode}
import play.core.server.ServerConfig
import play.filters.HttpFiltersComponents

abstract class ComponentTestBase extends PlaySpec with ITTestApplicationFactory with BaseOneServerPerTest {

  import play.api.mvc._
  import play.api.routing.sird._
  import play.api.test._
  import play.core.server.Server

  case class LeagueClient(testFilePath: String)

  case class MockedRequest(request: RequestHeader, response: Result)

  def withLeagueClient[T](block: WSClient => T): T = {

    val mockServerPort = app.configuration.get[Int]("mockServerPort")

    Server.withApplicationFromContext(config = ServerConfig(port = Some(mockServerPort), mode = Mode.Test)) {
      context =>
        new BuiltInComponentsFromContext(context) with HttpFiltersComponents {

          override def router: Router = SimpleRouter {
            case GET(p"/lol/static-data/v3/champions") =>
              Action { _ =>
                Results.Ok.sendResource("lol/static-data/v3/champions.json")(fileMimeTypes)
              }
          }
        }.application
    } { implicit mockServerPort =>
      WsTestClient.withClient { client =>
        block(client)
      }
    }
  }
}
