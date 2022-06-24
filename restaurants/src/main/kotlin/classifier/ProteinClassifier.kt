package app.jopiter.restaurants.classifier

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
class ProteinClassifier(
  @Value("classpath:classified_items/protein.csv")
  private val datasetLines: InputStream,
) {

  private val classifiableDataset by lazy {
    datasetLines.bufferedReader().readLines()
      .map { it.split("|") }
      .map { ClassifiableRow(it[0], it.drop(1)) }
  }

  private val classifier by lazy { Classifier(classifiableDataset) }

  fun classify(protein: String): ProteinClassification {
    val result = classifier.classify(protein)

    val (column1, column2, column3, column4) = result.predictedColumns
    return ProteinClassification(column1, column2, column3, column4)
  }

}

data class ProteinClassification(
  val foodGroup: String,
  val preparation: String,
  val cut: String,
  val color: String,
)
