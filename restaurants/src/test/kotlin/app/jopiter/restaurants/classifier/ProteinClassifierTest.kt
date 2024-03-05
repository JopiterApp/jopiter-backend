package app.jopiter.restaurants.classifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ProteinClassifierTest : FunSpec({

  val target = ProteinClassifier("classified_items/protein.csv".loadCsv())

  test("Smoke test") {
    val result = target.classify("Almôndega acebolada ao shoyo")

    result shouldBe ProteinClassification("bovina", "ao molho leve", "alcatra, almôndega, bife de contra filé, bife de patinho, coxão duro, lagarto, patinho/coxão mole", "Marrom/Bege")

    result.foodGroup shouldBe "bovina"
    result.preparation shouldBe "ao molho leve"
  }

})