package config

import com.softwaremill.macwire.wire
import components.statsBuilder.StatsBuilderModule
import controllers.AssetsComponents
import play.api.ApplicationLoader.Context
import play.api.cache.ehcache.EhCacheComponents
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, LoggerConfigurator, Mode}
import router.Routes

class LeagueApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with StatsBuilderModule
  with AssetsComponents
  with I18nComponents
  with AhcWSComponents
  with EhCacheComponents
  with play.filters.HttpFiltersComponents {

  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val mode: Mode = context.environment.mode

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }
}
