import data.{InvalidatedIngredient, ValidatedIngredient, RawLists}
import request.{callParser, callValidator}

import com.twitter.util.Future

import io.circe.{Error, Decoder, Json, ParsingFailure}
import io.circe.parser.parse

object parseingredients {

  implicit val decodeIngredient: Decoder[InvalidatedIngredient] = Decoder.forProduct3("name", "unit", "qty")(InvalidatedIngredient.apply)

  def isIngredientList(l: List[InvalidatedIngredient]): Boolean =
    if (l.isEmpty) false
    else l.count {
      case InvalidatedIngredient(_, Some(_), Some(_)) => true
      case _ => false
    } / l.length.toFloat >= .42 // some arbitrary number

  def decodeJSON[A](s: String)(jsonDecoder: Json => Either[Error, A]): A = {
    val decoded: Either[Error, A] = for {
      json <- parse(s)
      decoded <- jsonDecoder(json)
    } yield decoded
    decoded.getOrElse(throw new Exception("couldn't decode"))
  }

  def validateJSON(j: Json): Either[Error, Boolean] = j
    .hcursor
    .downField("parsed")
    .values
    .toRight(ParsingFailure("couldn't validate", throw new Exception()))
    .map(_.nonEmpty)

  def convertInvalidatedIngredient(i: InvalidatedIngredient): ValidatedIngredient =
    ValidatedIngredient.tupled(InvalidatedIngredient.unapply(i).get)

  def split(l: List[List[InvalidatedIngredient]]): (List[ValidatedIngredient], List[InvalidatedIngredient]) = {
    val splitIngredients = l
      .filter(isIngredientList) // take out most entries that aren't ingredients
      .flatten // combine to one list of ingredients
      .partition { // separate between invalidated / validated ingredients
        case InvalidatedIngredient(_, None, None) => false
        case _ => true
      }
    splitIngredients.copy(_1 = splitIngredients._1.map(convertInvalidatedIngredient))
  }

  def validateIngredients(invalidatedIngredients: List[InvalidatedIngredient]): Future[List[ValidatedIngredient]] = for {
    validationJSON: Seq[String] <- Future.collect(invalidatedIngredients.map(callValidator))
    validatedIngredients: List[ValidatedIngredient] = validationJSON.map(decodeJSON(_)(validateJSON))
      .toList
      .zip(invalidatedIngredients) // zip sketchy ingredients with boolean values
      .filter { case (bool, _) => bool } // filter out ones that are invalid
      .map { case (_, ingredient) => convertInvalidatedIngredient(ingredient) }
  } yield validatedIngredients

  def foodifyText(l: RawLists): Future[List[ValidatedIngredient]] = for {
    response: String <- callParser(l)
    allIngredients: List[List[InvalidatedIngredient]] = decodeJSON(response) { _.hcursor.get[List[List[InvalidatedIngredient]]]("result")}
    (validatedIngredients, invalidatedIngredients) = split(allIngredients)
    newlyValidatedIngredients <- validateIngredients(invalidatedIngredients)
    ingredients: List[ValidatedIngredient] = validatedIngredients ::: newlyValidatedIngredients
    if ingredients.nonEmpty
  } yield ingredients

}
