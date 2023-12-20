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

            xtest("Mundane item parse") {
                parsed.mundaneItems shouldContainExactlyInAnyOrder it.mundane.toList()
            }
        }
    }

})

private val examples = listOf(
    Example(
        1,
        "Acompanhamentos: Arroz / Feijão / Arroz Integral\nPrato principal: Frango assado\nOpção Vegetariana: Ovo pizzaiolo (lactose),\nGuarnição: Polenta\nSaladas: Catalonha, cenoura c/ beterraba\nSobremesa: Melão / Suco: Laranja",
        "Frango assado",
        "Ovo pizzaiolo (lactose)",
        "Melão",
        ""
    ),
    Example(
        1,
        "Acompanhamentos: Arroz / Feijão Preto / Arroz Integral\nPrato principal: Bife de panela\nOpção Vegetariana: PTS ao sugo\nGuarnição: Macarrão alho e óleo (glúten),\nSaladas: Repolho roxo, abóbora\nSobremesa:  Gelatina de limão / banana / Suco: Laranja",
        "Bife de panela",
        "Pts ao sugo",
        "Gelatina de limão",
        ""
    ),
    Example(
        2,
        "Arroz/Feijão/ Arroz integral/Saladas: Diversas/Filé de coxa Assado/Opção do Prato Principal: PVT alemão/Polenta cremosa/Sobremesa: Romeu e Julieta/ Abacaxi/Mini Pão e Suco",
        "Filé de coxa assado",
        "Pvt alemão",
        "Romeu e julieta",
        ""
    ),
    Example(
        2,
        "Arroz/Feijão/ Arroz integral/Saladas: Diversas/Estrogonofe de carne /Opção do Prato Principal: Estrogonofe de grão de bico/Batata palha/Sobremesa: Banana caramelada/ Mamão/Mini Pão e Suco",
        "Estrogonofe de carne",
        "Estrogonofe de grão de bico",
        "Banana caramelada",
        ""
    ),
    Example(
        3,
        "Arroz/Feijão/ Arroz integral/Saladas: Diversas/Filé de coxa Assado/Opção do Prato Principal: PVT alemão/Polenta cremosa/Sobremesa: Romeu e Julieta/ Abacaxi/Mini Pão e Suco",
        "Filé de coxa assado",
        "Pvt alemão",
        "Romeu e julieta",
        ""
    ),
    Example(3, "Fechado", null, null, null),
    Example(
        5,
        "Arroz branco/Arroz integral/Feijão carioca\nPrato principal: Carne assada ao molho madeira\nOpção vegetariana: Bolinho de lentilha (CONTÉM GLÚTEN),   \nGuarnição: Purê de batata \nSalada: Alface - Cenoura - Abóbora cozida \nSobremesa: Paçoca\nSuco de Morango e Mini pão francês",
        "Carne assada ao molho madeira",
        "Bolinho de lentilha (contém glúten),",
        "Paçoca",
        ""
    ),
    Example(
        5,
        "Arroz branco/Arroz integral/Feijão carioca\nPrato principal: Peixe a portuguesa\nOpção vegetariana: Escondidinho de PTS   \nGuarnição: Creme de cenoura\nSalada: Alface - Pepino - Beterraba cozida \nSobremesa: Paçoca\nSuco de Morango e Mini pão francês",
        "Peixe a portuguesa",
        "Escondidinho de pts",
        "Paçoca",
        ""
    ),
    Example(
        6,
        "Arroz / feijão / arroz integral\nFrango assado\nOpção: Ervilha com legumes\nRepolho refogado\nSalada de beterraba\nPão de mel\nMinipão / refresco",
        "Frango assado",
        "Ervilha com legumes",
        "Pão de mel",
        ""
    ),
    Example(
        6,
        "Arroz / feijão preto / arroz integral\nBife de caçarola com molho ferrugem\nOpção: PVT com vagem\nBatata doce corada\nSalada de alface\nBanana\nMinipão / refresco",
        "Bife de caçarola com molho ferrugem",
        "Pvt com vagem",
        "Banana",
        ""
    ),
    Example(
        7,
        "Arroz / feijão / arroz integral\nFilé de de peito de frango à pizzaiolo\nOpção: Omelete com alho-poró\nLegumes à juliana\nSalada de agrião\nSagú de maracujá\nMinipão / refresco",
        "Filé de de peito de frango à pizzaiolo",
        "Omelete com alho-poró",
        "Sagú de maracujá",
        ""
    ),
    Example(7, "Fechado", null, null, null, "mudane"),
    Example(
        8,
        "Arroz / feijão / arroz integral\nFrango xadrez\nOpção: Bolinho de PVT\nAbobrinha com manjericão\nSalada de escarola\nRomeu e julieta\nMinipão / refresco",
        "Frango xadrez",
        "Bolinho de pvt",
        "Romeu e julieta",

        ),
    Example(
        8,
        "Arroz / feijão / arroz integral\nLombo com molho de ervas\nOpção: PVT com cogumelos e tomate\nCenoura com vagem\nSalada de pepino\nLaranja\nMinipão / refresco",
        "Lombo com molho de ervas",
        "Pvt com cogumelos e tomate",
        "Laranja",
        ""
    ),
    Example(
        9,
        "Arroz / feijão / arroz integral\nLagarto com molho ferrugem\nOpção: PVT com alho-poró\nCenoura com vagem\nSalada de almeirão\nGelatina de uva \nMinipão / refresco",
        "Lagarto com molho ferrugem",
        "Pvt com alho-poró",
        "Gelatina de uva",
        ""
    ),
    Example(
        9,
        "Arroz / feijão / arroz integral\nLinguiça à escabeche\nOpção: Lasanha de brócolis com ricota\nQuiabo refogado\nSalada de escarola\nBanana \nMinipão / refresco",
        "Linguiça à escabeche",
        "Lasanha de brócolis com ricota",
        "Banana",
        ""
    ),
    Example(
        11,
        "Arroz / feijão preto / arroz integral\nCupim assado com molho madeira\nOpção: Hambúrguer de feijão branco\nAbóbora ao forno\nSalada de mix de folhas\nCurau\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Cupim assado com molho madeira",
        "Hambúrguer de feijão branco",
        "Curau",
        ""
    ),
    Example(
        11,
        "Arroz / feijão / arroz integral\nFilé de coxa de frango com molho de gergelim\nOpção: PVT com gergelim\nBerinjela com uva passas\nSalada de alface\nMamão\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Filé de coxa de frango com molho de gergelim",
        "Pvt com gergelim",
        "Mamão",
        ""
    ),
    Example(
        12,
        "Arroz / feijão / arroz integral \nFilé de coxa de frango com molho de ervas\nOpção: Fricassê de PVT\nEscarola refogada\nSalada cenoura\nMaçã\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Filé de coxa de frango com molho de ervas",
        "Fricassê de pvt",
        "Maçã",
        ""
    ),
    Example(12, "FECHADO", null, null, null, "mudane"),
    Example(
        13,
        "Arroz / arroz integral / feijão carioca\nHambúrguer à pizzaiolo\nOpção: Hambúrguer de PVT à pizzaiolo\nBatata ao forno\nSaladas: Alface / beterraba / feijão fradinho \nMaria mole / banana\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Hambúrguer à pizzaiolo",
        "Hambúrguer de pvt à pizzaiolo",
        "Maria mole",
        ""
    ),
    Example(
        13,
        "Arroz / arroz integral / feijão carioca\nLagarto com molho roti\nOpção: Moussaka vegetariana\nVagem sauté\nSaladas: Catalonha / cenoura cozida / grão-de-bico\nSagu / melão\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Lagarto com molho roti",
        "Moussaka vegetariana",
        "Sagu",
        ""
    ),
    Example(
        14,
        "Arroz / arroz integral / feijão carioca\nHambúrguer a pizzaiolo \nOpção: Hambúrguer de PVT\nBatata corada\nSalada de alface\nGoiabada\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Hambúrguer a pizzaiolo",
        "Hambúrguer de pvt",
        "Goiabada",
        ""
    ),
    Example(
        14,
        "Arroz / arroz integral / feijão carioca\nLagarto com molho roti\nOpção: Moussaka vegetariana\nVagem sauté\nSalada de catalonha\nBanana\nMinipão / refresco\n\n**Os Restaurantes Universitários não fornecem copos descartáveis. Tragam suas canecas.**",
        "Lagarto com molho roti",
        "Moussaka vegetariana",
        "Banana",
        ""
    ),
    Example(
        17,
        "Arroz / feijão preto / arroz integral\nSalsicha americana\nOpção: Grão-de-bico com tomate, cebola e salsa\nMacarrão ao alho e óleo\nSalada de alface\nGelatina com maçã\nMinipão / refresco",
        "Salsicha americana",
        "Grão-de-bico com tomate, cebola e salsa",
        "Gelatina com maçã",
        ""
    ),
    Example(
        17,
        "Arroz / feijão preto / arroz integral\nPanqueca de carne\nOpção: Panqueca de PVT\nChuchu com milho\nSalada de repolho\nMamão\nMinipão / refresco",
        "Panqueca de carne",
        "Panqueca de pvt",
        "Mamão",
        ""
    ),
    Example(
        19,
        "Linguiça Assada \nOp veg: PTS Clara à Primavera\nMandioca Ensopada\nAlface\nBerinjela\nMaçã\nArroz / Feijão / Integral \nSuco de Abacaxi\n808 Kcal/ 755 Kcal",
        "Linguiça assada",
        "Pts clara à primavera",
        "Maçã",
        ""
    ),
    Example(
        19,
        "Lagarto ao Molho Madeira\nOp veg: Hambúrguer de Soja\nRepolho Refogado\nAgrião com Almeirão \nPepino \nMelão \nArroz / Feijão / Integral \nSuco de Tangerina\n762 Kcal/ 702 Kcal",
        "Lagarto ao molho madeira",
        "Hambúrguer de soja",
        "Melão",
        ""
    ),
    Example(
        20,
        "BOLINHO DE CARNE AO MOLHO BARBECUE\n(musculo),\n152 KCAL\nLASANHA DE BERINJELA\n122KCAL\nCHUCHU REFOGADO\n 19KCAL\nALFACE\n9 kcal\nREPOLHO VERDE\n25 KCAL\nABÓBORA COZIDA\n31 KCAL\nARROZ BRANCO\n138kcal\nARROZ INTEGRAL\n116kcal\nFEIJÃO CARIOCA\n86 kcal\nMINI PÃO FRANCES\n75 kcal\nMELANCIA26 KCAL\nSUCO DE UVA\n48 kcal",
        "Bolinho de carne ao molho barbecue (musculo)",
        "Lasanha de berinjela",
        "Melancia",
        ""
    ),
    Example(
        20,
        "LINGUIÇA CHAPEADA\n143 KCAL\nQUIBE DE ABÓBORA PAULISTA  COM PTS\n177 kcal\nFAROFA DE CEBOLA\n88KCAL\nALFACE\n9 kcal\nPEPINO\n24 kcal\nJILÓ\n27 KCAL\nARROZ BRANCO\n138kcal\nARROZ INTEGRAL\n116kcal\nFEIJÃO CARIOCA\n86 kcal\nMINI PÃO FRANCES\n75 kcal\nMELANCIA\n30 KCAL\nSUCO DE UVA\n48 kcal",
        "Linguiça chapeada",
        "Quibe de abóbora paulista  com pts",
        "Melancia",
        ""
    ),
    Example(
        23,
        "Arroz / feijão preto / arroz integral\nSalsicha americana\nOpção: Grão-de-bico com tomate, cebola e salsa\nMacarrão ao alho e óleo\nSalada de alface\nGelatina com maçã\nMinipão / refresco",
        "Salsicha americana",
        "Grão-de-bico com tomate, cebola e salsa",
        "Gelatina com maçã",
        ""
    ),
    Example(23, "Fechado", null, null, null, ""),
)

private class Example(
    val restaurant: Int,
    val text: String,
    val main: String?,
    val vegetarian: String?,
    val dessert: String?,
    vararg val mundane: String
)