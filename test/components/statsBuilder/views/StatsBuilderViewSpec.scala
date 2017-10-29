package components.statsBuilder.views

import controllers.AssetsFinder
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import testutils.SharedTestFixtures

class StatsBuilderViewSpec extends PlaySpec with MockFactory with OneInstancePerTest with SharedTestFixtures {

  implicit lazy val assetsFinder: AssetsFinder = mock[AssetsFinder]

  "The championsList view" must {

    val assetsTestLocation: String = "assets/test/bootstrap.css"

    assetsFinder.path _ expects "lib/bootstrap/css/bootstrap.css" returning assetsTestLocation

    lazy val view = components.statsBuilder.views.html.championsList(champions.model)(assetsFinder)

    lazy val viewAsString = contentAsString(view)

    "load assets from the correct location" in {
      viewAsString must include(s"""<link rel="stylesheet" media="screen" href="$assetsTestLocation">""")
    }
    "display the given champions name" in {
      viewAsString must include(
        """<td>Wukong</td>""".stripMargin
      )
    }
    "display the given champions title" in {
      viewAsString must include(
        """<td>the Monkey King</td>""".stripMargin
      )
    }
    "display multiple champions" in {

      lazy val view = components.statsBuilder.views.html.championsList(champions2.model)(assetsFinder)

      lazy val viewAsString = contentAsString(view)

      viewAsString must include(
        """<td>Annie</td>""".stripMargin
      )

      viewAsString must include(
        """<td>Wukong</td>""".stripMargin
      )
    }
  }

}
