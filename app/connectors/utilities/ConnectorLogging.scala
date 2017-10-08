package connectors.utilities

import play.api.libs.ws.{WSRequest, WSResponse}
import utilities.Logging

import scala.concurrent.Future
import scala.language.implicitConversions

trait ConnectorLogging extends Logging {

  implicit def withRequestLogging(wSRequest: WSRequest): Future[WSResponse] = {
    logger.debug(
      s"""
         |Requesting champions list
         |  * Method: ${wSRequest.method}
         |  * URL : ${wSRequest.url}
         |  * Headers : ${wSRequest.headers.keys}
         |  * Query Parameters : ${wSRequest.queryString}
         |""".stripMargin)
    wSRequest.execute()
  }
}
