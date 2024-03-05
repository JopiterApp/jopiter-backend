package app.jopiter.restaurants.classifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class VegetarianClassifierTest : FunSpec({

  val target = VegetarianClassifier("classified_items/vegetarian.csv".loadCsv())

  test("Smoke test") {
    val result = target.classify("Pvt agridoce")

    result shouldBe VegetarianClassification(
      "PTS",
      "grelhado/refogado",
    )
  }
})