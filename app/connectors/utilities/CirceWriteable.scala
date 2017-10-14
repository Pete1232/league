package connectors.utilities

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec

trait CirceWriteable {
  implicit def contentTypeOf_CirceJson(implicit codec: Codec): ContentTypeOf[io.circe.Json] = {
    ContentTypeOf[io.circe.Json](Some(ContentTypes.JSON))
  }

  implicit def writeableOf_CirceJson(implicit codec: Codec): Writeable[io.circe.Json] = {
    Writeable(obj => codec.encode(obj.noSpaces))
  }
}
