package testutils

import config.LeagueApplicationComponents
import org.scalatestplus.play.FakeApplicationFactory
import play.api.inject.DefaultApplicationLifecycle
import play.api.{Application, ApplicationLoader, Configuration, Environment}
import play.core.DefaultWebCommands

import scala.concurrent.ExecutionContext

trait ITTestApplicationFactory extends FakeApplicationFactory {

  implicit val ec: ExecutionContext = ExecutionContext.global

  def config: Configuration = Configuration.load(env)

  def env: Environment = Environment.simple()
  def context: ApplicationLoader.Context = ApplicationLoader.Context(
    environment = env,
    sourceMapper = None,
    webCommands = new DefaultWebCommands(),
    initialConfiguration = config,
    lifecycle = new DefaultApplicationLifecycle()
  )
  lazy val components = new LeagueApplicationComponents(context)

  def fakeApplication(): Application = components.application
}
