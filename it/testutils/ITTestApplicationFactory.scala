package testutils

import config.LeagueApplicationComponents
import org.scalatestplus.play.FakeApplicationFactory
import play.api.inject.DefaultApplicationLifecycle
import play.api.{Application, ApplicationLoader, Configuration, Environment}
import play.core.DefaultWebCommands

trait ITTestApplicationFactory extends FakeApplicationFactory {

  def env: Environment = Environment.simple()
  def context: ApplicationLoader.Context = ApplicationLoader.Context(
    environment = env,
    sourceMapper = None,
    webCommands = new DefaultWebCommands(),
    initialConfiguration = Configuration.load(env),
    lifecycle = new DefaultApplicationLifecycle()
  )
  lazy val components = new LeagueApplicationComponents(context)

  def fakeApplication(): Application = components.application
}
