import data.{Ingredient, RawLists}
import request.{callParser, callValidator}
import scala.concurrent.Future

import cats.data.EitherT
import cats.implicits._

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

  def decodeJSON[A](s: String)(jsonDecoder: Json => Either[Error, A]): Either[Error, A] = for {
    json <- parse(s)
    decoded <- jsonDecoder(json)
  } yield decoded

  def validate(j: Json): Either[io.circe.Error, Boolean] = j
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

  def validateSketchyIngredients(sketchyIngredients: List[Ingredient]): EitherT[Future, Error, List[Ingredient]] = for {
    validationJSON <- EitherT.right(sketchyIngredients.traverse(callValidator))
    validation: List[Boolean] <- EitherT(Future { validationJSON.traverse(decodeJSON(_)(validate)) })
    validatedIngredients: List[Ingredient] = validation
      .zip(sketchyIngredients) // zip sketchy ingredients with boolean values
      .filter { case (bool, _) => bool } // filter out ones that are invalid
      .map { case (_, value) => value }
  } yield validatedIngredients

  def foodifyText(l: RawLists): EitherT[Future, Error, List[Ingredient]] = for {
    response: String <- EitherT.right(callParser(l))
    allIngredients: List[List[Ingredient]] <- EitherT(Future {decodeJSON(response) { _.hcursor.get[List[List[Ingredient]]]("result")} })
    (normalIngredients: List[Ingredient], sketchyIngredients: List[Ingredient]) = split(allIngredients)
    validatedIngredients: List[Ingredient] <- validateSketchyIngredients(sketchyIngredients)
    ingredients = normalIngredients ::: validatedIngredients
    if ingredients.nonEmpty
  } yield ingredients

}
