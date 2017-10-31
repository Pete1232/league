package connectors.models

abstract class ErrorResponse extends RuntimeException {
  val message: String

  override def toString: String = s"ErrorResponse($message)"
}

case class LolErrorStatus(message: String, status_code: Int)

case class LolErrorResponse(status: LolErrorStatus) extends ErrorResponse {
  override val message: String = status.message
}

case class JsonErrorResponse(message: String) extends ErrorResponse
