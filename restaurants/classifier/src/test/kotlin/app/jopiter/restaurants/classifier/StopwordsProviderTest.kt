package app.jopiter.restaurants.classifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

class StopwordsProviderTest : FunSpec({
  val target = StopwordsProvider()

  test("Return words from portuguese_stopwords file") {
    target.portugueseStopwordsCsv shouldContain "algumas"
    target.portugueseStopwordsCsv shouldContain "lá"
    target.portugueseStopwordsCsv shouldContain "último"
  }
})
