package utilities

import play.api.Logger

trait Logging {
  val logger = Logger(this.getClass)
}
