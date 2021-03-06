package testutils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatestplus.play.PlaySpec
import play.api.i18n.DefaultMessagesApi
import play.api.mvc.{BodyParsers, ControllerComponents, DefaultActionBuilder}

import scala.concurrent.ExecutionContext

abstract class ControllerTestBase extends PlaySpec with GeneratorDrivenPropertyChecks with MockFactory
  with OneInstancePerTest with SharedTestFixtures {

  implicit val system: ActorSystem = ActorSystem(this.getClass.getSimpleName)

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val ec: ExecutionContext = ExecutionContext.global

  lazy val mockControllerComponents: ControllerComponents = stub[ControllerComponents]

  mockControllerComponents.actionBuilder _ when() returns DefaultActionBuilder(new BodyParsers.Default)
  mockControllerComponents.messagesApi _ when() returns new DefaultMessagesApi()
}
