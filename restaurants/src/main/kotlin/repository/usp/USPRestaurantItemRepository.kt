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

import app.jopiter.restaurants.model.Period.Dinner
import app.jopiter.restaurants.model.Period.Lunch
import app.jopiter.restaurants.model.RestaurantItem
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import java.time.LocalDate
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter.ofPattern

private const val RequestTimeoutMillis = 2_000

class USPRestaurantItemRepository(
    private val uspBaseUrl: String,
    private val parsers: Map<Int, MenuParser>,
    private val uspHash: String
) {

    fun fetch(restaurantId: Int): Set<RestaurantItem> {
        val (_, _, result) = "$uspBaseUrl/menu/$restaurantId"
            .httpPost(listOf("hash" to uspHash))
            .timeoutRead(RequestTimeoutMillis)
            .responseObject<MenuResponse>()

        return result.fold(
            { it.toRestaurantItems(restaurantId, parsers[restaurantId]!!) },
            { if (it.causedByInterruption) emptySet() else throw it }
        )
    }

    private class MenuResponse(val meals: List<MenuMeals>) {
        
        fun toRestaurantItems(restaurantId: Int, parser: MenuParser) = meals.flatMap {
            listOf(
                RestaurantItem(restaurantId, it.localDate, Lunch, it.lunch.calories, parser.parse(it.lunch.menu)),
                RestaurantItem(restaurantId, it.localDate, Dinner, it.dinner.calories, parser.parse(it.dinner.menu))
            )

        }.toSet()
    }

    private data class MenuMeals(val dinner: MenuPeriod, val lunch: MenuPeriod, val date: String) {
        val localDate: LocalDate by lazy { parse(date, ofPattern("dd/MM/yyyy")) }
    }

    private data class MenuPeriod(val menu: String, val calories: Long? = null)
}
