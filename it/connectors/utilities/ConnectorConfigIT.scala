package connectors.utilities

import com.typesafe.config.ConfigException
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import play.api.{ApplicationLoader, Configuration}
import testutils.ITTestApplicationFactory

class ConnectorConfigIT extends PlaySpec {
  "The application config" must {
    "be able to load valid connector config" in new ITTestApplicationFactory {
      await {
        fakeApplication().stop()
      }
    }
    "fail if the apiKey s missing on app startup" in new ITTestApplicationFactory {
      override def context: ApplicationLoader.Context = {
        super.context.copy(
          initialConfiguration = Configuration.reference
        )
      }

      intercept[ConfigException] {
        fakeApplication().stop()
      }

      components.ehCacheManager.removeAllCaches()
    }
  }
}
