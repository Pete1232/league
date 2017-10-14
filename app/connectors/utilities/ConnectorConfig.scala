package connectors.utilities

import play.api.{Configuration, Mode}

class ConnectorConfig(config: Configuration, mode: Mode) {

  private val configRoot: String = s"riotgames.developer"

  val apiKey: String = config.get[String](s"$configRoot.apiKey")

  private val proxyServiceConfig: Configuration = config.get[Configuration](s"$configRoot.proxy.service")

  val scheme: String = proxyServiceConfig.get[String]("scheme")
  val host: String = proxyServiceConfig.get[String]("host")
  val platform: String = proxyServiceConfig.get[String]("platform")
  val region: String = proxyServiceConfig.get[String]("region")
}
