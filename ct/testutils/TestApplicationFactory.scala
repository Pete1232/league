package testutils

import config.LeagueApplicationComponents
import org.scalatestplus.play.FakeApplicationFactory
import play.api.inject.DefaultApplicationLifecycle
import play.api.{Application, ApplicationLoader, Configuration, Environment}
import play.core.DefaultWebCommands

trait TestApplicationFactory extends FakeApplicationFactory {

  private val env = Environment.simple()
  private val context = ApplicationLoader.Context(
    environment = env,
    sourceMapper = None,
    webCommands = new DefaultWebCommands(),
    initialConfiguration = Configuration.load(env),
    lifecycle = new DefaultApplicationLifecycle()
  )
  val components = new LeagueApplicationComponents(context)

  def fakeApplication(): Application = components.application
}
