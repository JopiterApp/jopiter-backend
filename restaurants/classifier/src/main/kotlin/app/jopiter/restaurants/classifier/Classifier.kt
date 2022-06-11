/*
 * Jopiter APP
 * Copyright (C) 2021 Leonardo Colman Lopes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.jopiter.restaurants.classifier

import io.michaelrocks.bimap.BiMap
import io.michaelrocks.bimap.HashBiMap
import smile.classification.ovr
import smile.classification.svm
import smile.math.kernel.LinearKernel
import smile.classification.Classifier as SmileClassifier

class Classifier(
  rows: List<ClassifiableRow>,
) {
  private val documents = rows.map { it.name }
  private val preProcessor = PreProcesser(documents)

  private val columns = List(rows[0].columns.size) { i ->
    rows.map {
      it.columns[i]
    }
  }

  private val predictors = columns.map {
    val categories = it.distinct()

    if (categories.size == 2) {
      booleanPredictor(categories, it)
    } else {
      multiclassPredictor(categories, it)
    }
  }


  private fun booleanPredictor(classes: List<String>, values: List<String>): Predictor {
    val classMap = HashBiMap.create(mapOf(-1 to classes[0], 1 to classes[1]))
    val columnTruth = values.map { classMap.inverse[it]!! }.toIntArray()
    val classifier = svm(preProcessor.preparedDocuments, columnTruth, LinearKernel(), 0.5)
    return Predictor(classifier, classMap)
  }

  private fun multiclassPredictor(classes: List<String>, values: List<String>): Predictor {
    check(classes.none { it.isEmpty() })
    val classMap = classes.mapIndexed { index, s -> index to s }.toMap(HashBiMap())
    val columnTruth = values.map { classMap.inverse[it]!! }.toIntArray()
    val classifier = ovr(preProcessor.preparedDocuments, columnTruth, ::svmClassifier)
    return Predictor(classifier, classMap)
  }

  private fun svmClassifier(x: Array<DoubleArray>, y: IntArray) = svm(x, y, LinearKernel(), 0.5)

  fun classify(name: String): ClassifiedRow {
    val preProcessedName = preProcessor.preProcess(name)

    val predictedValues = predictors.map { it.predict(preProcessedName) }

    return ClassifiedRow(name, predictedValues)
  }
}

data class ClassifiableRow(
  val name: String,
  val columns: List<String>,
)

data class ClassifiedRow(
  val name: String,
  val predictedColumns: List<String>,
)

class Predictor(
  private val classifier: SmileClassifier<DoubleArray>,
  private val classes: BiMap<Int, String>,
) {

  fun predict(preProcessedName: DoubleArray): String {
    val predictedIndex = classifier.predict(preProcessedName)
    return classes[predictedIndex]!!
  }
}


