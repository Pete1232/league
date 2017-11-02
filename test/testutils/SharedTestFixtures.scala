package testutils

import java.io.File

import connectors.getAllChampions.models.Champions
import connectors.models.LolErrorResponse
import org.scalacheck.Shrink

import scala.io.Source

trait SharedTestFixtures {

  implicit def noShrink[T]: Shrink[T] = Shrink(_ => Stream.empty[T])

  private val staticDataRoute = "test/resources/lol/static-data/v3"

  private def getTestFile(fileName: String): String = {
    val filePath = new File(s"$staticDataRoute/$fileName")
    Source.fromFile(filePath).mkString
  }

  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._
  import io.circe.syntax._

  object champions {
    val model: Champions = decode[Champions](getTestFile("champions.json")).right.get
    val asJson: Json = model.asJson
  }

  object champions2 {
    val model: Champions = decode[Champions](getTestFile("champions.2.json")).right.get
    val asJson: Json = model.asJson
  }

  object forbidden {
    val model: LolErrorResponse = decode[LolErrorResponse](getTestFile("forbidden.json")).right.get
    val asJson: Json = model.asJson
  }

}
