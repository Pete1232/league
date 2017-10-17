package testutils

import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, OneInstancePerTest, TestSuite}
import play.api.cache.ehcache.EhCacheComponents
import play.api.inject.{ApplicationLifecycle, DefaultApplicationLifecycle}
import play.api.{Configuration, Environment}

import scala.concurrent.ExecutionContext

trait MockCacheComponents extends EhCacheComponents with BeforeAndAfterEach with BeforeAndAfterAll {
  this: TestSuite =>

  override def beforeEach(): Unit = {
    super.beforeEach()
    defaultCacheApi.removeAll()
  }

  override def afterEach(): Unit = {
    defaultCacheApi.removeAll()
    super.afterEach()
  }

  override def afterAll(): Unit = {
    ehCacheManager.removeAllCaches()
    super.afterAll()
  }

  lazy val env: Environment = Environment.simple()

  override def environment: Environment = env

  override def configuration: Configuration = Configuration.load(env)

  override def applicationLifecycle: ApplicationLifecycle = new DefaultApplicationLifecycle()

  override def actorSystem: ActorSystem = ActorSystem(this.getClass.getSimpleName)

  override implicit def executionContext: ExecutionContext = ExecutionContext.global
}
