package components.statsBuilder

import components._
import connectors.getAllChampions.models.ChampionStats
import utilities.Logging

object StatsBuilderService extends Logging {
  def getMaxBaseHp(championStats: ChampionStats): Double = {
    import championStats._
    val res = hp + hpperlevel * (MAX_LEVEL - 1)
    logger.debug(s"Calculated max level $res for $championStats")
    res
  }
}
