package connectors.utilities

import connectors.RIOT_TOKEN_HEADER_KEY
import play.api.Logger
import play.api.libs.ws.{WSRequest, WSResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

trait ConnectorLogging {

  val logger = Logger(this.getClass)

  implicit def withRequestLogging(wSRequest: WSRequest)
                                 (implicit ec: ExecutionContext): Future[WSResponse] = {
    logger.debug(
      s"""Request sent to external server
         |  * Method: ${wSRequest.method}
         |  * URL : ${wSRequest.url}
         |  * Headers : ${wSRequest.headers.keys}
         |  * Query Parameters : ${wSRequest.queryString}
         |  * API key (redacted): ${wSRequest.headers.get(RIOT_TOKEN_HEADER_KEY).map(_.map(_.take(20) + "..."))}
         |""".stripMargin)
    wSRequest.execute()
      .map { res =>
        logger.debug(
          s"""Received response
             |  * Status: ${res.status}
             |  * Headers: ${res.headers}
             |  * Body: ${res.body.take(100)} (limited to 100 characters)
          """.stripMargin)
        res
      }
  }
}
