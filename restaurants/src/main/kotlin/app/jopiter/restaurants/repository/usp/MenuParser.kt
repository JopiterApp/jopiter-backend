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

import app.jopiter.restaurants.model.Period
import app.jopiter.restaurants.model.RestaurantItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import kotlin.text.RegexOption.IGNORE_CASE

fun interface MenuParser {
  fun parse(menu: String): Menu
}

val luizDeQueirozParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val (main, veg, dessert) = items.find("Prato principal:", "Opção Vegetariana:", "Sobremesa:")
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val centralSaoCarlosParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val (veg, dessert) = items.find("principal:", "Sobremesa:")
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val pirassunungaParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val (main, veg, des) = items.find("Prato Principal:", "Opção Vegetariana:", "Sobremesa:")
  Menu(main, veg, des, items.cleanStrings(main, veg, des), it)
}

val centralParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val veg = items.find("Opção:").single()
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  val dessert = items.getOrNull(items.lastIndex - 2)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val quadrilateroParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val veg = items.find("Opção").single().cleanString()
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  val dessert = items.getOrNull(items.lastIndex - 3)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val eachParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val veg = items.find("Opção:").single()
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  val dessert = items.getOrNull(items.lastIndex - 4)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val largoSaoFranciscoParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val veg = items.find("Opção:").single()
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  val dessert = items.getOrNull(items.lastIndex - 3)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val centralRibeiraoParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val main = items.firstOrNull()?.cleanString()
  val veg = items.getOrNull(1)?.cleanString()
  val des = items.getOrNull(5)?.cleanString()
  Menu(main, veg, des, items.cleanStrings(main, veg, des), it)
}

val bauruParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val main = items[0].cleanString()
  val veg = items[1].cleanString()
  val dessert = items[items.lastIndex - 1].cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val lorenaParser = MenuParser {
  val items = it.cleanItems()
  if (items.size < 3) return@MenuParser closedMenuParser.parse(it)
  val veg = items.find("Opção:").single()
  val main = items.getOrNull(items.findIndex(veg) - 1)?.cleanString()
  val dessert = items.getOrNull(items.lastIndex - 2)?.cleanString()
  Menu(main, veg, dessert, items.cleanStrings(main, veg, dessert), it)
}

val closedMenuParser = MenuParser { Menu(null, null, null, emptyList(), it) }

private fun List<String>.find(vararg containing: String) =
  filter { str -> containing.any { str.contains(it, true) } }.map { it.cleanString() }

private fun List<String>.findIndex(value: String?) =
  if (value == null) -1 else indexOfFirst { it.contains(value, true) }

private fun List<String>.cleanStrings(vararg specialItems: String?) =
  (map { it.cleanString() } - specialItems.toSet()).filterNot { it.isNullOrBlank() }.filterNotNull()

private fun String.cleanItems() =
  replace("\n(", " (")
    .split("\n", "/")
    .map { it.replace(Regex("[()]*\\d*,*\\.*\\d*\\s*kcal[(())]*", IGNORE_CASE), "") }
    .map { it.trim(',') }
    .filter { it.isNotBlank() }
    .filterNot { it.contains("Marmitex", true) }


private fun String.cleanString() =
  replace("c/", "com", true).substringAfter(":").trim().lowercase().replaceFirstChar { it.titlecase() }

data class Menu(
  val mainItem: String?,
  val vegetarianItem: String?,
  val dessertItem: String?,
  val mundaneItems: List<String>,
  val unparsedMenu: String
)

@Suppress("FunctionNaming")
fun RestaurantItem(restaurantId: Int, date: LocalDate, period: Period, calories: Int?, menu: Menu) = menu.run {
  RestaurantItem(
    restaurantId, date, period, calories, mainItem, vegetarianItem, dessertItem, mundaneItems, unparsedMenu
  )
}

@Configuration
class ParsersConfig {
  @Bean
  fun parsers() = parsers
}

val parsers: Map<Int, MenuParser> = mapOf(
  1 to luizDeQueirozParser,
  2 to centralSaoCarlosParser,
  3 to centralSaoCarlosParser,
  5 to pirassunungaParser,
  6 to centralParser,
  7 to centralParser,
  8 to centralParser,
  9 to centralParser,
  11 to quadrilateroParser,
  12 to quadrilateroParser,
  13 to eachParser,
  14 to largoSaoFranciscoParser,
  17 to lorenaParser,
  19 to centralRibeiraoParser,
  20 to bauruParser,
  23 to lorenaParser,
) + listOf(4, 10).map { it to closedMenuParser }
