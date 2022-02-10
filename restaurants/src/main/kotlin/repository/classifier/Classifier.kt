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

import org.apache.lucene.analysis.br.BrazilianStemmer
import smile.classification.ovr
import smile.classification.svm
import smile.math.kernel.LinearKernel
import smile.nlp.bag
import smile.nlp.tfidf
import smile.nlp.vectorize
import smile.classification.Classifier as SmileClassifier

private val PortugueseStopWords by lazy {
  Classifier::class.java.classLoader.getResourceAsStream("./classified_items/portuguese_stopwords.txt").reader()
    .readText()
}

class PreProcesser(
  documents: List<String>,
  private val csvStopWords: String = PortugueseStopWords,
  private val stemmer: BrStemmer = BrStemmer,
) {

  private val corpus = documents.map { it.bagged() }
  private val vocabulary = corpus.flatMap { it.keys }.toTypedArray()
  private val vectorizedCorpus = corpus.map { it.vectorized() }

  val preProcessedDocuments = tfidf(vectorizedCorpus).toTypedArray()

  fun preProcess(document: String) = vectorize(vocabulary, document.bagged())

  private fun String.bagged() = bag(csvStopWords, stemmer::stem)

  private fun Map<String, Int>.vectorized() = vectorize(vocabulary, this)
}

class Classifier(
  rows: List<ClassifiableRow>
) {
  private val documents = rows.map { it.name }
  private val preProcessor = PreProcesser(documents)

  private val columns = List(rows[0].columns.size) { i ->
    rows.map { it.columns[i] }
  }

  private val predictors = columns.map {
    val classes = it.distinct()
    val classifiedColumn = it.map { classes.indexOf(it) }.toIntArray()

    val classifier = ovr(preProcessor.preProcessedDocuments, classifiedColumn) { x, y ->
      svm(x, y, LinearKernel(), 0.5)
    }

    Predictor(classifier, classes)
  }

  fun classify(name: String): ClassifiedRow {
    val preProcessedName = preProcessor.preProcess(name)

    val predictedValues = predictors.map { it.predict(preProcessedName) }

    return ClassifiedRow(name, predictedValues)
  }
}

data class ClassifiableRow(
  val name: String,
  val columns: List<String>
)

data class ClassifiedRow(
  val name: String,
  val predictedColumns: List<String>
)

object BrStemmer : BrazilianStemmer() {

  public override fun stem(term: String?): String {
    return super.stem(term).orEmpty()
  }
}

class Predictor(
  private val classifier: SmileClassifier<DoubleArray>,
  private val classes: List<String>
) {

  fun predict(preProcessedName: DoubleArray): String {
    val predictedIndex = classifier.predict(preProcessedName)
    return classes[predictedIndex]
  }
}
