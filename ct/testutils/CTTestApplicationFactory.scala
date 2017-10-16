package testutils

import config.LeagueApplicationComponents
import org.scalatestplus.play.FakeApplicationFactory
import play.api.inject.DefaultApplicationLifecycle
import play.api.{Application, ApplicationLoader, Configuration, Environment}
import play.core.DefaultWebCommands

import scala.concurrent.ExecutionContext

trait CTTestApplicationFactory extends FakeApplicationFactory {

  implicit val ec: ExecutionContext = ExecutionContext.global

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
