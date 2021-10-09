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
    Example(6, "Arroz/feijão/arroz integral\nBife de contrafilé acebolado\nOpção: PVT com alho-poró\nMandioquinha com salsa\nSalada de acelga\nDoce de banana\nMinipão",
    "Bife de contrafilé acebolado", "Pvt com alho-poró", "Doce de banana","Arroz", "Feijão", "Arroz integral", "Mandioquinha com salsa", "Salada de acelga", "Minipão"),
        Example(7, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado",
    null, null, null),
    Example(8, "Informamos que a partir de amanhã, 24/03, terça-feira, os Restaurantes Universitários estarão fechados em decorrência da quarentena decretada pelo Governo do Estado",
        null, null, null),
    Example(9, "Arroz / Feijão \nFilé de peito de frango empanado\nOpção: PVT com alho-poró\nCreme de milho\nSalada de almeirão\nBanana  \nMinipão", "Filé de peito de frango empanado", "Pvt com alho-poró", "Banana", "Arroz", "Feijão", "Creme de milho", "Salada de almeirão", "Minipão"),
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
    Example(20, "CARNE ASSADA (COXÃO DURO) (336,4 kcal)\n\nOVO COZIDO\n\nALFACE\n\nREPOLHO ROXO E ABACAXI\n\nABÓBORA EM CUBOS COZIDA\n\n\nCOUVE REFOGADO\n\nARROZ BRANCO\n\nARROZ INTEGRAL\n\nFEIJÃO CARIOCA\n\nLARANJA PÊRA\n\nSUCO \n\nMINI PÃO FRANCÊS (25G)", "Carne assada (coxão duro)", "Ovo cozido", "Laranja pêra", "Alface", "Repolho roxo e abacaxi", "Abóbora em cubos cozida", "Couve refogado", "Arroz branco", "Arroz integral", "Feijão carioca", "Suco", "Mini pão francês (25g)")
)

private class Example(val restaurant: Int, val text: String, val main: String?, val vegetarian: String?, val dessert: String?, vararg val mundane: String)