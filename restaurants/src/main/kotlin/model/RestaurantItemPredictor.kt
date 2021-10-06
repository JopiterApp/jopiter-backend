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

package app.jopiter.restaurants.model

import org.apache.commons.csv.CSVFormat.DEFAULT
import org.apache.commons.csv.CSVParser
import org.apache.lucene.analysis.br.BrazilianStemmer
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import smile.classification.OneVersusRest
import smile.classification.ovr
import smile.classification.svm
import smile.math.kernel.LinearKernel
import smile.nlp.bag
import smile.nlp.tfidf
import smile.nlp.vectorize
import javax.annotation.PostConstruct
import kotlin.random.Random

@Component
class RestaurantItemPredictor(
        private val foodGroupPredictor: FoodGroupPredictor,
        private val preparationPredictor: PreparationPredictor,
        private val colorPredictor: ColorPredictor,
) {

    fun predict(dish: String) = ItemPredictionResult(
            foodGroupPredictor.predict(dish),
            preparationPredictor.predict(dish),
            colorPredictor.predict(dish)
    )

    data class ItemPredictionResult(
            val foodGroup: Map<String, String>,
            val preparation: Map<String, String>,
            val color: Map<String, String>,
    )
}

@Component
class FoodGroupPredictor(
        @Value("classpath:protein/main_protein_food_group.csv") csvFile: Resource,
) : Predictor(
        csvFile,
        listOf("ave", "bovina", "ovo", "peixe", "suína", "processada", "massa"))

@Component
class PreparationPredictor(
        @Value("classpath:protein/main_protein_preparation.csv") csvFile: Resource,
) : Predictor(
        csvFile,
        listOf("ao_molho_gorduroso", "assado", "cozido", "frito", "grelhado_refogado", "ao_molho_leve")
)

@Component
class ColorPredictor(
        @Value("classpath:protein/main_protein_color.csv") csvFile: Resource
) : Predictor(
        csvFile,
        listOf("Marrom/Bege", "Amarelo/Laranja", "Branco/Cinza", "Vermelho", "Verde", "Roxo/preto", "Misto")
)

abstract class Predictor(csvFile: Resource, private val categories: List<String>) {
    @Value("classpath:stop_words.txt")
    lateinit var stopWordsRes: Resource

    private val dataSet by lazy { CSVParser(csvFile.file.bufferedReader(), DEFAULT).map { Item(it[0], it[1]) } }
    private val stopWords by lazy { stopWordsRes.file.readText() }

    private lateinit var features: Array<String>
    private lateinit var model: OneVersusRest<DoubleArray>

    @PostConstruct
    fun train() {
        val (train, test) = dataSet.partition { Random.nextDouble() <= 0.9 }

        val corpus = train.map { it.name.bag(stopWords, BrStemmer::stem) }
        features = corpus.flatMap { it.keys }.toTypedArray()

        val vectors = corpus.map { vectorize(features, it) }
        val data = tfidf(vectors)

        model = ovr(data.toTypedArray(), train.map { categories.indexOf(it.category) }.toIntArray()) { x, y ->
            svm(x, y, LinearKernel(), 0.5)
        }
    }

    fun predict(dish: String): Map<String, String> {
        val preProcessed = vectorize(features, dish.bag(stopWords, BrStemmer::stem))
        val posteriori = DoubleArray(7)
        model.predict(preProcessed, posteriori)
        return categories.mapIndexed { index, itemCategory ->
            itemCategory to posteriori[index]
        }.toMap().mapValues { (_, d) -> String.format("%.2f", d) }
    }
}

object BrStemmer : BrazilianStemmer() {

    public override fun stem(term: String?): String {
        return super.stem(term).orEmpty()
    }
}

data class Item(val name: String, val category: String)
