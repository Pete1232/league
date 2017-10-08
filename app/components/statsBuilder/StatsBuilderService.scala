package components.statsBuilder

import components._
import connectors.getAllChampions.models.ChampionStats

object StatsBuilderService {

  def getMaxBaseHp(championStats: ChampionStats): Double = {
    import championStats._
    hp + hpperlevel * (MAX_LEVEL - 1)
  }
}
