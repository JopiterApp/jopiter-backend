package app.jopiter.restaurants.classifier

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class VegetarianClassifier(
  @Value("classpath:classified_items/vegetarian.csv")
  private val datasetLines: InputStream,
) {

  private val classifiableDataset by lazy {
    datasetLines.bufferedReader().readLines()
      .map { it.split("|") }
      .map { ClassifiableRow(it[0], it.drop(1)) }
  }

  private val classifier by lazy { Classifier(classifiableDataset) }

  fun classify(vegetarian: String): VegetarianClassification {
    val result = classifier.classify(vegetarian)

    val (column1, column2) = result.predictedColumns
    return VegetarianClassification(column1, column2)
  }

}

data class VegetarianClassification(
  val foodGroup: String,
  val preparation: String,
)
