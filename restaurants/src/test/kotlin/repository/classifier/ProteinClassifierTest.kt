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
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlin.random.Random

class ProteinClassifierTest : FunSpec({

  val datasetLines = "classified_items/protein.csv".loadCsv()
  val target = ProteinClassifier(datasetLines.toStream())

  test("Creates a model with 80%+ accuracy") {
    val (trainData, testData) = datasetLines.partition { Random.nextDouble() <= 0.90 }

    val targetWithReducedDataset = ProteinClassifier(trainData.toStream())


    var rightGuesses = 0
    testData.map { it.split(",") }.forEach { (name, group, prep) ->
      val (_, predictedGroup, predictedPrep) = targetWithReducedDataset.classify(name)
      if(group == predictedGroup.description) rightGuesses++
      if(prep == predictedPrep.description) rightGuesses++
    }

    val accuracy = rightGuesses.toDouble() / (testData.size * 2) // Once for group once for prep

    accuracy shouldBeGreaterThan 0.80
    println("Accuracy: $accuracy")

  }
})

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
private fun String.loadCsv() =
  ProteinClassifierTest::class.java.classLoader.getResourceAsStream(this).reader().readLines()

private fun List<String>.toStream() = joinToString("\n").byteInputStream()
