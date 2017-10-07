package connectors.config

import javax.inject.{Inject, Singleton}

import play.api.Configuration

@Singleton
class ConnectorConfig @Inject()(config: Configuration) {

  private val configRoot: String = "riotgames.developer"

  val apiKey: String = config.get[String](s"$configRoot.apiKey")

  private val proxyServiceConfig: Configuration = config.get[Configuration](s"$configRoot.proxy.service")

  val scheme: String = proxyServiceConfig.get[String]("scheme")
  val host: String = proxyServiceConfig.get[String]("host")
  val platform: String = proxyServiceConfig.get[String]("platform")
  val region: String = proxyServiceConfig.get[String]("region")
}
