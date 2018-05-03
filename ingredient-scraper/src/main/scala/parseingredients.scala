import data.{Ingredient, RawLists}
import request.{callParser, callValidator}

import com.twitter.util.Future

import io.circe.{Error, Decoder, Json, ParsingFailure}
import io.circe.parser.parse

object parseingredients {

  implicit val decodeIngredient: Decoder[Ingredient] = Decoder.forProduct3("name", "unit", "qty")(Ingredient.apply)

  def isIngredientList(l: List[Ingredient]): Boolean =
    if (l.isEmpty) false
    else l.count {
      case Ingredient(_, Some(_), Some(_)) => true
      case _ => false
    } / l.length.toFloat >= .42 // some arbitrary number

  def decodeJSON[A](s: String)(jsonDecoder: Json => Either[Error, A]): A = {
    val decoded: Either[io.circe.Error, A] = for {
      json <- parse(s)
      decoded <- jsonDecoder(json)
    } yield decoded
    decoded.getOrElse(throw new Exception("couldn't decode"))
  }

  def validate(j: Json): Either[Error, Boolean] = j
    .hcursor
    .downField("parsed")
    .values
    .toRight(ParsingFailure("couldn't validate", throw new Exception()))
    .map(_.nonEmpty)

  def split(l: List[List[Ingredient]]): (List[Ingredient], List[Ingredient]) = l
    .filter(isIngredientList) // take out most entries that aren't ingredients
    .flatten // combine to one list of ingredients
    .partition { // separate between sketchy and normal ingredients
    case Ingredient(_, None, None) => false
    case _ => true
  }

  def validateSketchyIngredients(sketchyIngredients: List[Ingredient]): Future[List[Ingredient]] = for {
    validationJSON: Seq[String] <- Future.collect(sketchyIngredients.map(callValidator))
    validatedIngredients: List[Ingredient] = validationJSON.map(decodeJSON(_)(validate))
      .toList
      .zip(sketchyIngredients) // zip sketchy ingredients with boolean values
      .filter { case (bool, _) => bool } // filter out ones that are invalid
      .map { case (_, value) => value }
  } yield validatedIngredients

  def foodifyText(l: RawLists): Future[List[Ingredient]] = for {
    response: String <- callParser(l)
    allIngredients: List[List[Ingredient]] = decodeJSON(response) { _.hcursor.get[List[List[Ingredient]]]("result")}
    (normalIngredients: List[Ingredient], sketchyIngredients: List[Ingredient]) = split(allIngredients)
    validatedIngredients: List[Ingredient] <- validateSketchyIngredients(sketchyIngredients)
    ingredients = normalIngredients ::: validatedIngredients
    if ingredients.nonEmpty
  } yield ingredients

}
