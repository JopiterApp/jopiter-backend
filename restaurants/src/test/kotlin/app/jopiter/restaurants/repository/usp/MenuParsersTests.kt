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
    "Acompanhamentos: Arroz / Feijão/Arroz Integral\nPrato principal: Pernil acebolado\nOpção Vegetariana: Ervilha c/ creme de abobora\nGuarnição: Farofa c/azeitona e couve flor\nSaladas: Alface/ berinjela c/ pimentões\nSobremesa: Melão",
    "Pernil acebolado",
    "Ervilha com creme de abobora",
    "Melão"
  ),
  Example(
    1,
    "Acompanhamentos: Arroz / Feijão /Arroz Integral\nPrato principal: Carne de panela\nOpção Vegetariana: Ovo frito\nGuarnição: Macarrão/ brócolis\nSaladas: Alface /Abobrinha cozida\nSobremesa: Laranja",
    "Carne de panela",
    "Ovo frito",
    "Laranja"
  ),
  Example(
    2,
    "Arroz/Feijão/ Arroz integral/\nSaladas: Diversas/\nBife ao molho ferrugem/\nOpção do Prato Principal: Lasanha de ricota e espinafre/\nRepolho com azeitonas/\nSobremesa: Doce de batata/Laranja/\nMini Pão e Suco",
    "Bife ao molho ferrugem",
    "Lasanha de ricota e espinafre",
    "Doce de batata"
  ),
  Example(
    2,
    "Arroz/Feijão/ Arroz integral/\nSaladas: Diversas/\nCarne em cubos ao molho roty/\nOpção do Prato Principal: Omelete à margaritta/\nVagem à provençal/\nSobremesa: Pudim de maria mole com calda de goiabada/Abacaxi/\nMini Pão e Suco",
    "Carne em cubos ao molho roty",
    "Omelete à margaritta",
    "Pudim de maria mole com calda de goiabada"
  ),
  Example(
    3,
    "Arroz/Feijão/ Arroz integral/\nSaladas: Diversas/\nBife ao molho ferrugem/\nOpção do Prato Principal: Lasanha de ricota e espinafre/\nRepolho com azeitonas/\nSobremesa: Doce de batata/Laranja/\nMini Pão e Suco",
    "Bife ao molho ferrugem",
    "Lasanha de ricota e espinafre",
    "Doce de batata"
  ),
  Example(3, "Fechado", null, null, null),
  Example(
    5,
    "Arroz branco/Arroz integral/Feijão carioca\nPrato principal: Carne louca\nOpção vegetariana: Hambúrguer de feijão branco\nGuarnição: Mix de folhas refogada\nSalada: Alface - Beterraba - Couve flor\nSobremesa: Melancia\nSuco: Abacaxi\nMini pão francês",
    "Carne louca",
    "Hambúrguer de feijão branco",
    "Melancia"
  ),
  Example(
    5,
    "Arroz branco/Arroz integral/Feijão carioca\nPrato principal: Filé de sobrecoxa crocante (Contém Glúten)\nOpção vegetariana: Abobrinha recheada com PTS\nGuarnição: Purê de mandioquinha (Contém Lactose)\nSalada: Alface, Repolho verde, Berinjela com cebola\nSobremesa: Pudim de baunilha (Contém Lactose)\nSuco: Maracujá\nMini pão francês",
    "Filé de sobrecoxa crocante (contém glúten)",
    "Abobrinha recheada com pts",
    "Pudim de baunilha (contém lactose)"
  ),
  Example(
    6,
    "Arroz/feijão/arroz integral\nCarne assada com molho de mostarda\nOpção: PVT caipira\nCaponata de abobrinha\nSalada de almeirão\nBanana\nMinipão/refresco",
    "Carne assada com molho de mostarda",
    "Pvt caipira",
    "Banana"
  ),
  Example(
    6,
    "Arroz/feijão/arroz integral\nLinguiça acebolada\nOpção: Macarrão com molho de shimeji (V)\nRepolho refogado com passas\nSalada de beterraba ralada\nMexerica\nMinipão/refresco",
    "Linguiça acebolada",
    "Macarrão com molho de shimeji (v)",
    "Mexerica"
  ),
  Example(
    7,
    "Arroz/feijão /arroz integral\nFilé de frango com molho de laranja\nOpção: Almôndegas de PVT com molho roti\nMandioca na manteiga\nSalada almeirão \nBolinho de baunilha\nMinipão/ Refresco",
    "Filé de frango com molho de laranja",
    "Almôndegas de pvt com molho roti",
    "Bolinho de baunilha"
  ),
  Example(7, "FECHADO", null, null, null),
  Example(
    8,
    "Em razão do jogo da Seleção Brasileira, funcionará excepcionalmente das 11h15 às 13h.\nArroz/Feijão/ Arroz integral\nBife com molho roti \nOpção:  PVT com tomate e salsa\nMacarrão ao sugo \nSalada de almeirão \nBolinho de chocolate\nMini Pão/ Refresco",
    "Bife com molho roti",
    "Pvt com tomate e salsa",
    "Bolinho de chocolate"
  ),
  Example(
    8,
    "Arroz / Feijão Preto/ Arroz Integral \nCarne em cubos ao molho roty\nOpção: Grão de bico ao vinagrete\nBrócolis e couve flor refogado \nSalada de Beterraba\nCocada branca\nMini pão / Refresco",
    "Carne em cubos ao molho roty",
    "Grão de bico ao vinagrete",
    "Cocada branca"
  ),
  Example(
    9,
    "Arroz/feijão preto/arroz integral\nPeixe empanado\nOpção: Omelete de forno\nChuchu refogado\nSalada de catalônia\nLaranja\nMinipão/ refresco",
    "Peixe empanado",
    "Omelete de forno",
    "Laranja"
  ),
  Example(
    9,
    "Arroz/ feijão/ arroz integral\nBife de contrafilé grelhado \nOpção: Almôndegas de Feijão preto \nRepolho com passas \nSalada de tomate com pepino  \nBanana\nMinipão/ refresco",
    "Bife de contrafilé grelhado",
    "Almôndegas de feijão preto",
    "Banana"
  ),
  Example(11, "Fechado para reforma a partir de 27.04.2026.", null, null, null),
  Example(
    12,
    "Arroz/feijão/ arroz integral\nPeixe no fubá\nOpção: PVT com mandioca\nCreme de espinafre \nSaladas: Almeirão\nDoce de goiaba industrializado\nMini pão/refresco",
    "Peixe no fubá",
    "Pvt com mandioca",
    "Doce de goiaba industrializado"
  ),
  Example(12, "FECHADO", null, null, null),
  Example(
    13,
    "Em razão do jogo da Seleção Brasileira, funcionará excepcionalmente das 11h15 às 13h.\nArroz/ Arroz integral/ Feijão carioca\nFilé de coxa de frango acebolado \nOpção:  PVT com ricota \nPurê misto\nSalada: Repolho\nSobremesas:  Laranja\nMini pão/refresco",
    "Filé de coxa de frango acebolado",
    "Pvt com ricota",
    "Laranja"
  ),
  Example(
    13,
    "Arroz/ Arroz integral/ Feijão carioca\nLinguiça com molho barbecue\nOpção:  Feijoada vegetariana \nCouve refogada\nSaladas: Cenoura ralada \nSobremesas: Sagu \nMini pão/refresco",
    "Linguiça com molho barbecue",
    "Feijoada vegetariana",
    "Sagu"
  ),
  Example(
    14,
    "Em razão do jogo da Seleção Brasileira, funcionará excepcionalmente das 11h15 às 13h.\nArroz/ Arroz integral/ Feijão carioca\nFilé de coxa de frango acebolado \nOpção:  PVT com ricota \nPurê misto\nSalada: Repolho \nSobremesas: Melão\nMini pão/refresco",
    "Filé de coxa de frango acebolado",
    "Pvt com ricota",
    "Melão"
  ),
  Example(
    14,
    "Arroz/ Arroz integral/ Feijão carioca\nLinguinça com molho barbecue\nOpção:  Feijoada vegetariana \nCouve refogada\nSaladas:  Cenoura ralada \nSobremesas: Cocada\nMini pão/refresco",
    "Linguinça com molho barbecue",
    "Feijoada vegetariana",
    "Cocada"
  ),
  Example(
    17,
    "Arroz / feijão / arroz integral\nFrango à moda caipira\nOpção: Bolinho caipira com PVT\nBatata doce com milho\nSalada de alface com cenoura\nPavê de paçoquinha\nMinipão / Refresco",
    "Frango à moda caipira",
    "Bolinho caipira com pvt",
    "Pavê de paçoquinha"
  ),
  Example(
    17,
    "Arroz / feijão / arroz integral\nBife de alcatra com molho de páprica\nOpção: Hambúrguer de PVT\nBeterraba sauté\nSalada de repolho\nGelatina com maçã\nMinipão / Refresco",
    "Bife de alcatra com molho de páprica",
    "Hambúrguer de pvt",
    "Gelatina com maçã"
  ),
  Example(
    19,
    "Carne de Panela (Coxão Duro) \nAlmondega Vegetariana\nChuchu Refogado\nRepolho \nBeterraba Ralada \nMaçã\nArroz / Feijão / Integral\n1110 Kcal/ 1000 Kcal",
    "Carne de panela (coxão duro)",
    "Almondega vegetariana",
    "Maçã"
  ),
  Example(
    19,
    "Bife Bovino ao Molho Madeira (Coxão Mole) \nHambúrguer Vegetariano\nMandioca Ensopada \nRúcula \nTomate com cebola \nLaranja\nArroz / Feijão / Integral\n1160 Kcal/ 1092 Kcal",
    "Bife bovino ao molho madeira (coxão mole)",
    "Hambúrguer vegetariano",
    "Laranja"
  ),
  Example(
    20,
    "BIFE GRELHADO\nBOLINHO DE BRÓCOLIS COM RICOTA (CONTÉM LACTOSE E GLÚTEN)\nREPOLHO REFOGADO COM MILHO E ERVILHA \nSALADA DE ALFACE\nSALADA TOMATE\nMOLHO VINAGRETE\nMOLHO TÁRTARO \nARROZ BRANCO\nARROZ INTEGRAL\nFEIJÃO PRETO\nMINI PÃO FRANCÊS \nFARINHA DE MANDIOCA TORRADA FINA \nLARANJA\nSUCO DE MANGA",
    "Bife grelhado",
    "Bolinho de brócolis com ricota (contém lactose e glúten)",
    "Laranja"
  ),
  Example(
    20,
    "FILE DE FRANGO A MILANESA (CONTÉM GLÚTEN)\nOVOS MEXIDOS\nBATATA SAUTÉ\nSALADA DE ACELGA\nSALADA DE LENTILHA\nMOLHO VINAGRETE\nMOLHO MOSTARDA       \nARROZ BRANCO\nARROZ INTEGRAL\nFEIJÃO CARIOCA \nMINI PÃO FRANCÊS \nFARINHA DE MANDIOCA TORRADA FINA \nMAÇA\nSUCO DE CAJU",
    "File de frango a milanesa (contém glúten)",
    "Ovos mexidos",
    "Maça"
  ),
  Example(
    23,
    "Arroz / feijão / arroz integral\nFrango à moda caipira\nOpção: Bolinho caipira com PVT\nBatata doce com milho\nSalada de alface com cenoura\nPavê de paçoquinha\nMinipão / Refresco",
    "Frango à moda caipira",
    "Bolinho caipira com pvt",
    "Pavê de paçoquinha"
  ),
  Example(23, "", null, null, null),
)

private class Example(
  val restaurant: Int,
  val text: String,
  val main: String?,
  val vegetarian: String?,
  val dessert: String?,
  vararg val mundane: String
)
