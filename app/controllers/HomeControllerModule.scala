package controllers

import play.api.i18n.Langs
import play.api.mvc.ControllerComponents

trait HomeControllerModule {

  import com.softwaremill.macwire._

  def langs: Langs

  def controllerComponents: ControllerComponents

  lazy val greeterController: HomeController = wire[HomeController]
}
