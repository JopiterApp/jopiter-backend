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

package app.jopiter.restaurants.repository.usp

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class MenuParsersTests : FunSpec({

    examples.forEach {
        context("Parser for menu ${it.restaurant}") {
            val parser = parsers.getValue(it.restaurant)
            val parsed = parser.parse(it.text)

            test("Main item parse") {
                parsed.mainItem shouldBe it.main
            }

            test("Vegetarian item parse") {
                parsed.vegetarianItem shouldBe it.vegetarian
            }

            test("Dessert item parse") {
                parsed.dessertItem shouldBe it.dessert
            }

            test("Mundane item parse") {
                parsed.mundaneItems shouldContainExactlyInAnyOrder it.mundane.toList()
            }
        }
    }

})

private val examples = listOf(
    Example(1, "Acompanhamentos: Arroz / Feijão / Arroz Integral\nPrato principal: Isca de Carne à Moda Chinesa\nPrato principal Vegetariano: Cuscuz de Lentilha\nGuarnição: Macarrão Alho e Óleo\nSaladas: Alface, Tomate, Beterraba Cozida\nSobremesa: Laranja / Suco: Maracujá", "Isca de carne à moda chinesa", "Cuscuz de lentilha", "Laranja", "Arroz", "Feijão", "Arroz integral", "Macarrão alho e óleo", "Alface, tomate, beterraba cozida", "Maracujá"),
    Example(2, "Arroz/Feijão/Saladas: Escarola e tomate/Bife ao molho de cebola/Opção prato principal: Ovo frito/\nBatata gratinada/Sobremesa: Laranja",
        "Bife ao molho de cebola", "Ovo frito", "Laranja", "Arroz", "Feijão", "Escarola e tomate", "Batata gratinada"),
    Example(3, "Fechado", null, null, null),
    Example(4, "Fechado", null, null, null),
    Example(5, "Em virtude da Pandemia estamos servindo Marmitex \nArroz branco/Feijão carioca\nPrato Principal: Frango grelhado\nOpção Vegetariana: Ovo mexido\nGuarnição: Macarrão ao molho (Contém glúten) \nSalada: Alface/Cenoura", "Frango grelhado", "Ovo mexido", null, "Arroz branco", "Feijão carioca", "Macarrão ao molho (contém glúten)", "Alface", "Cenoura"),
    Example(6, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado",
    null, null, null),
        Example(7, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado",
    null, null, null),
    Example(8, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado",
        null, null, null),
    Example(9, "Informamos que, a partir de 01/01/2021, o\nRestaurante das Químicas não mais fornecerá\ntalheres descartáveis, tendo em vista a\nsustentabilidade e atendimento à Lei Nº 17.261,\ndo município de São Paulo, a qual proíbe o\nfornecimento de copos e utensílios de material\nplástico em estabelecimentos de alimentação.",
        null, null, null),
    Example(11, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado.",
        null, null, null),
    Example(12, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado.",
        null, null, null),
    Example(13, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado.",
        null, null, null),
    Example(14, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado.",
        null, null, null),
    Example(17, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado.",
        null, null, null),
    Example(19, "Arroz / Feijão\nBife Acebolado (Patinho)\nJardineira de Legumes\nMelão\n\nOp veg: Grão de bico à Primavera\n\nGeral: 885 Kcal\nVegetariano: 812 Kcal",
    "Bife acebolado (patinho)", "Grão de bico à primavera", "Melão", "Arroz", "Feijão", "Jardineira de legumes"),
    Example(20, "Pernil suíno ao forno 393,8 kcal\nOvo frito 223,5 kcal\nAlface e abacaxi 25,7 kcal\nCenoura vick contem lactose 84,9 kcal\nArroz 243,6 kcal\nFeijão 164,8 kcal\nBanana 91,5 kcal\nSuco laranja 57 kcal", "Pernil suíno ao forno", "Ovo frito", "Banana", "Alface e abacaxi", "Cenoura vick contem lactose", "Arroz", "Feijão", "Suco laranja")
)

private class Example(val restaurant: Int, val text: String, val main: String?, val vegetarian: String?, val dessert: String?, vararg val mundane: String)