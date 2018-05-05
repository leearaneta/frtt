object data {

  type RawLists = List[List[String]]

  sealed trait Ingredient
  final case class InvalidatedIngredient(name: String, unit: Option[String], qty: Option[String]) extends Ingredient
  final case class ValidatedIngredient(name: String, unit: Option[String], qty: Option[String]) extends Ingredient

  case class Recipe(name: String, ingredients: List[ValidatedIngredient])
  case class ParserPayload(method: String, params: RawLists, jsonrpc: String = "2.0", id: Int = 0)
  case class Address(url: String)
  case class SeleniumDomain(domainName: String, locatorType: String, locatorName: String)

  // TODO: hack training data so these abbreviations aren't necessary
  // TODO: dynamically save SeleniumDomains to database

  val abbreviations = Map(
    "tsp" -> "teaspoon",
    "tbsp" -> "tablespoon",
    " c " -> " cup ",
    "oz" -> "ounce",
    "lb" -> "pound"
  )
  val seleniumDomains = List(
    SeleniumDomain("yummly", "CLASS_NAME", "recipe-ingredients")
  )

}
