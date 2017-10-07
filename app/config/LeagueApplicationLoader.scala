package config

import play.api.ApplicationLoader.Context
import play.api._

class LeagueApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new LeagueApplicationComponents(context).application
}
