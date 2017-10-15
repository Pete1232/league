package components.statsBuilder.views

import connectors.getAllChampions.models.{Champion, ChampionStats, Champions}
import controllers.AssetsFinder
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._

class StatsBuilderViewSpec extends PlaySpec with MockFactory with OneInstancePerTest {

  implicit lazy val assetsFinder: AssetsFinder = mock[AssetsFinder]

  "The championsList view" must {

    val assetsTestLocation: String = "assets/test/bootstrap.css"

    assetsFinder.path _ expects "lib/bootstrap/css/bootstrap.css" returning assetsTestLocation

    val mockedChampionDetails = Champions(
      "champion",
      "7.20.2",
      Map(
        "Annie" -> Champion("Annie", "the Dark Child", ChampionStats(511.68, 76))
      )
    )

    lazy val view = components.statsBuilder.views.html.championsList(mockedChampionDetails)(assetsFinder)

    lazy val viewAsString = contentAsString(view)

    "load assets from the correct location" in {
      viewAsString must include(s"""<link rel="stylesheet" media="screen" href="$assetsTestLocation">""")
    }
    "display the given champions name" in {
      viewAsString must include(
        """<td>Annie</td>""".stripMargin
      )
    }
    "display the given champions title" in {
      viewAsString must include(
        """<td>the Dark Child</td>""".stripMargin
      )
    }
    "display multiple champions" in {

      val mockedChampionDetails = Champions(
        "champion",
        "7.20.2",
        Map(
          "Annie" -> Champion("Annie", "the Dark Child", ChampionStats(511.68, 76)),
          "Wukong" -> Champion("Wukong", "the Monkey King", ChampionStats(577.8, 85))
        )
      )

      lazy val view = components.statsBuilder.views.html.championsList(mockedChampionDetails)(assetsFinder)

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
