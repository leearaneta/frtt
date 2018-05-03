import AppConfig.{appID, appKey}
import data.{Ingredient, ParserPayload}

import java.net.URLEncoder
import io.circe.syntax._
import io.circe.generic.auto._

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.{Request, RequestBuilder, Response}
import com.twitter.io.Buf
import com.twitter.util.Future

object request {

  def callParser(l: List[List[String]]): Future[String] = {
    val jsonString: String = ParserPayload("parse_all", l).asJson.toString
    val client: Service[Request, Response] = ClientBuilder()
      .stack(Http.client)
      .hosts("tagger:4000")
      .build()
    val request: Request = RequestBuilder()
      .url("http://tagger:4000/jsonrpc")
      .buildPost(Buf.Utf8(jsonString))
    client(request).map(_.contentString)
  }

  def callValidator(i: Ingredient): Future[String] = {
    val client: Service[Request, Response] = ClientBuilder()
      .stack(Http.client)
      .hosts("api.edamam.com:443")
      .tls("api.edamam.com") // for https requests
      .build()
    val paramString = s"ingr=${URLEncoder.encode(i.name, "UTF-8")}&app_id=$appID&app_key=$appKey"
    val request = RequestBuilder()
      .url(s"https://api.edamam.com/api/food-database/parser?" + paramString)
      .buildGet()
    client(request).map(_.contentString)
  }

}
