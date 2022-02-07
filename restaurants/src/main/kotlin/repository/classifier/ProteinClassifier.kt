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

import org.apache.commons.csv.CSVFormat.DEFAULT
import org.apache.commons.csv.CSVParser
import org.apache.lucene.analysis.br.BrazilianStemmer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import smile.classification.ovr
import smile.classification.svm
import smile.math.kernel.LinearKernel
import smile.nlp.bag
import smile.nlp.tfidf
import smile.nlp.vectorize
import java.io.InputStream

@Configuration
class ProteinClassifierConfig(
  @Value("classpath:classified_items/protein.csv") val proteinCsv: Resource,
  @Value("classpath:classified_items/portuguese_stopwords.txt") val portugueseStopwords: Resource,
) {

  @Bean
  fun proteinClassifier() = ProteinClassifier(
    proteinCsv.inputStream,
    portugueseStopwords.inputStream.reader().readText()
  )
}

private val portugueseStopWords by lazy {
  ProteinClassifier::class.java.classLoader.getResourceAsStream("./classified_items/portuguese_stopwords.txt").reader()
    .readText()
}

@Service
class ProteinClassifier(
  csv: InputStream,
  private val commaSeparatedStopwords: String = portugueseStopWords,
) {

  private val dataset = CSVParser(csv.bufferedReader(), DEFAULT).map {
    ProteinItem(it[0], FoodGroup.getByDescription(it[1]), Preparation.getByDescription(it[2]))
  }

  private val corpus = dataset.map { it.name.bag(commaSeparatedStopwords, BrStemmer::stem) }
  private val vocabulary = corpus.flatMap { it.keys }.toTypedArray()
  private val vectorizedCorpus = corpus.map { vectorize(vocabulary, it) }
  private val preprocessedData = tfidf(vectorizedCorpus)

  private val groupModel =
    ovr(preprocessedData.toTypedArray(), dataset.map { it.foodGroup.ordinal }.toIntArray()) { x, y ->
      svm(x, y, LinearKernel(), 0.5)
    }

  private val prepModel =
    ovr(preprocessedData.toTypedArray(), dataset.map { it.preparation.ordinal }.toIntArray()) { x, y ->
      svm(x, y, LinearKernel(), 0.5)
    }

  fun classify(name: String): ProteinItem {
    val preProcessedName = vectorize(vocabulary, name.bag(commaSeparatedStopwords, BrStemmer::stem))

    val predictedGroup = groupModel.predict(preProcessedName)
    val predictedPreparation = prepModel.predict(preProcessedName)


    return ProteinItem(name, FoodGroup.values()[predictedGroup], Preparation.values()[predictedPreparation])
  }
}

data class ProteinItem(val name: String, val foodGroup: FoodGroup, val preparation: Preparation)

enum class FoodGroup(val score: Int, val description: String) {
  Ave(2, "Ave"),
  Bovina(1, "Bovina"),
  Ovo(2, "Ovo"),
  Peixe(2, "Peixe"),
  Suina(1, "Suína"),
  Processada(0, "Processada");

  companion object {
    fun getByDescription(description: String) = values().find { it.description == description }
      ?: throw IllegalStateException("Description isn't expected: $description")
  }
}

enum class Preparation(val score: Int, val description: String) {
  AoMolhoGorduroso(1, "Ao molho gorduroso"),
  Assado(2, "Assado"),
  Cozido(2, "Cozido"),
  Frito(0, "Frito"),
  GrelhadoRefogado(2, "Grelhado/Refogado"),
  AoMolhoLeve(2, "Ao molho leve");

  companion object {
    fun getByDescription(description: String) = values().find { it.description == description }
      ?: throw IllegalStateException("Description isn't expected: $description")
  }
}

private object BrStemmer : BrazilianStemmer() {

  public override fun stem(term: String?): String {
    return super.stem(term).orEmpty()
  }
}
