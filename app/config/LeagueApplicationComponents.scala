package config

import com.softwaremill.macwire.wire
import connectors.ConnectorModule
import controllers.{AssetsComponents, HomeControllerModule}
import play.api.ApplicationLoader.Context
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, LoggerConfigurator}
import router.Routes

class LeagueApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with HomeControllerModule
  with ConnectorModule
  with AssetsComponents
  with I18nComponents
  with AhcWSComponents
  with play.filters.HttpFiltersComponents {

  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }
}
