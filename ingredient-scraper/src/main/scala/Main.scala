import parsehtml.{getUnorderedLists, inferLists, prepareHTML}
import parseingredients.foodifyText
import data.{Ingredient, Recipe, Address}

import org.jsoup.nodes.Document

import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Await, Future}

import io.finch._
import io.finch.syntax._

import com.hypertino.inflector.English

object Main extends App {

  def foodifyHTML(f: Document => List[List[String]]) = f andThen foodifyText

  def foodify(d: Document): Future[List[Ingredient]] = foodifyHTML(getUnorderedLists)(d).rescue {
    case _ => foodifyHTML(inferLists)(d)
  }

  def singularize(s: String): String = English.singular(s)
  def dedupe(s: String): String = s.split(" ").distinct.mkString(" ")

  def formatName = singularize _ andThen dedupe
  def formatIngredient(i: Ingredient) = i.copy(name = formatName(i.name))

  def getRecipeFromHTML(d: Document): Future[Recipe] = {
    for {
      ingredients <- foodify(d)
      formattedIngredients = ingredients.map(formatIngredient)
    } yield Recipe(d.title, formattedIngredients)
  }
  // refactor parser and validator into helper function

  def execute: String => Future[Recipe] = prepareHTML andThen getRecipeFromHTML

  val parseEndpoint: Endpoint[Recipe] = post("parse" :: jsonBody[Address]) { a: Address => execute(a.url).map(Ok) }
  val service = parseEndpoint.toServiceAs[Application.Json]

  val policy: Cors.Policy = Cors.Policy(
    allowsOrigin = _ => Some("*"),
    allowsMethods = _ => Some(Seq("GET", "POST")),
    allowsHeaders = _ => Some(Seq("Content-Type"))
  )

  val corsService: Service[Request, Response] = new Cors.HttpFilter(policy).andThen(service)
  Await.ready(Http.server.serve(":8081", corsService))

}