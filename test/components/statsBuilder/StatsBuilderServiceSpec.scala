package components.statsBuilder

import connectors.getAllChampions.models.ChampionStats
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatestplus.play.PlaySpec

class StatsBuilderServiceSpec extends PlaySpec with GeneratorDrivenPropertyChecks {

  import StatsBuilderService._

  "getMaxBaseHp" must {
    "calculate the maximum health of Annie with known values" in {

      val hp: Double = 511.68
      val hpperlevel: Double = 76

      val champion = ChampionStats(hp, hpperlevel)

      getMaxBaseHp(champion) mustBe 1803.68
    }
    "calculate the maximum health of a champion" in {

      forAll { (hp: Double, hpperlevel: Double) =>
        val champion = ChampionStats(hp, hpperlevel)

        getMaxBaseHp(champion) mustBe (hp + hpperlevel * (components.MAX_LEVEL - 1))
      }
    }
  }
}
