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

import java.time.LocalDate

data class RestaurantItem(
    val restaurantId: Int,
    val date: LocalDate,
    val period: Period,
    val calories: Long?,
    val mainItem: String?,
    val vegetarianItem: String?,
    val dessertItem: String?,
    val mundaneItems: List<String>,
    val unparsedMenu: String,
    val restaurantName: String = Restaurant.getById(restaurantId).name
)

enum class Period { Lunch, Dinner }
