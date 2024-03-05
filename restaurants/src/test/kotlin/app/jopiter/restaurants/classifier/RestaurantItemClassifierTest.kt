package app.jopiter.restaurants.classifier

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec

class RestaurantItemClassifierTest : FunSpec({

  val proteinClassifier = ProteinClassifier("classified_items/protein.csv".loadCsv())
  val vegetarianClassifier = VegetarianClassifier("classified_items/vegetarian.csv".loadCsv())
  val dessertClassifier = DessertClassifier("classified_items/dessert.csv".loadCsv())


  val target = RestaurantItemClassifier(proteinClassifier, vegetarianClassifier, dessertClassifier)


  test("Warms up without trowing exceptions") {
    shouldNotThrowAny { target.warmupModel() }
  }

})
