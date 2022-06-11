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

import app.jopiter.restaurants.classifier.ClassifiableRow
import app.jopiter.restaurants.classifier.Classifier
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import kotlin.random.Random

fun classifierTest(csvName: String) = funSpec {
  context(csvName) {
    val datasetLines = "classified_items/$csvName.csv".loadCsv()
    val rows = datasetLines.map { it.split("|") }.map {
      val name = it[0]
      val columns = it.drop(1)
      ClassifiableRow(name, columns)
    }
    context("Creates a model with 80%+ accuracy") {
      val (trainData, testData) = rows.partition { Random.nextDouble() <= 0.80 }

      val target = Classifier(trainData)

      val realClassifications = testData.map { it.columns }
      val predictions = testData.map { target.classify(it.name).predictedColumns }

      val accuraciesPerColumn = List(predictions[0].size) { i ->
        val realColumn = realClassifications.map { it[i] }
        val predictedColumn = predictions.map { it[i] }

        val correctGuesses = realColumn.zip(predictedColumn).count { (truth, prediction) ->
          truth == prediction
        }.toDouble()

        correctGuesses / realColumn.size.toDouble()
      }

      accuraciesPerColumn.forEachIndexed { index, d ->
        test("Column $index") {
          println("Accuracy of column $index: $d")
          d shouldBeGreaterThanOrEqual 0.80
        }
      }
    }
  }
}

class ClassifierTests : FunSpec({
  include(classifierTest("protein"))
  include(classifierTest("side_dish"))
  include(classifierTest("vegetarian"))
  include(classifierTest("dessert"))
})

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
private fun String.loadCsv() =
  ClassifierTests::class.java.classLoader.getResourceAsStream(this).reader().readLines()

