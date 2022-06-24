package app.jopiter.restaurants.classifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DessertClassifierTest : FunSpec({

  val target = DessertClassifier("classified_items/dessert.csv".loadCsv())

  test("Smoke test") {
    val result = target.classify("Brigadeiro")
    result shouldBe DessertClassification(
      "doce",
      "ultraprocessado"
    )
  }
})