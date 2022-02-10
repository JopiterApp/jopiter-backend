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

package app.jopiter.restaurants.repository.classifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import kotlin.random.Random

class ClassifierTests : FunSpec({

  val datasetLines = "classified_items/protein.csv".loadCsv()
  val rows = datasetLines.map { it.split(",") }.map {
    val name = it[0]
    val columns = it.subList(1, it.size)
    ClassifiableRow(name, columns)
  }

  test("Creates a model with 80%+ accuracy") {
    val (trainData, testData) = rows.partition { Random.nextDouble() <= 0.90 }

    val targetWithReducedDataset = Classifier(trainData)


    var rightGuesses = 0
    testData.forEach { (name, columns) ->
      val (_, predictedColumns) = targetWithReducedDataset.classify(name)
      columns.zip(predictedColumns).forEach { (truth, predicted) ->
        if(truth == predicted) rightGuesses++
      }
    }

    val accuracy = rightGuesses.toDouble() / (testData.size * rows.first().columns.size)

    accuracy shouldBeGreaterThan 0.80
    println("Accuracy: $accuracy")

  }
})

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
private fun String.loadCsv() =
  ClassifierTests::class.java.classLoader.getResourceAsStream(this).reader().readLines()

