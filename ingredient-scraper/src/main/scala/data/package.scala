package object data {

  case class Ingredient(name: String, unit: Option[String], qty: Option[String])
  case class Recipe(name: String, ingredients: List[Ingredient])
  case class ParserPayload(method: String, params: List[List[String]], jsonrpc: String = "2.0", id: Int = 0)
  case class Address(url: String)
  case class SeleniumDomain(domainName: String, locatorType: String, locatorName: String)

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
