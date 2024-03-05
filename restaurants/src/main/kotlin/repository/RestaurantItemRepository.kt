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

package app.jopiter.restaurants.repository

import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.repository.postgres.PostgresRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.USPRestaurantItemRepository
import org.springframework.stereotype.Repository
import org.threeten.extra.YearWeek
import java.time.LocalDate

@Repository
class RestaurantItemRepository(
    private val uspRestaurantItemRepository: USPRestaurantItemRepository,
    private val postgresRestaurantItemRepository: PostgresRestaurantItemRepository
) {
    fun get(restaurantId: Int, dates: Set<LocalDate>) = dates.flatMap { fetch(restaurantId, it) }.toSet()

    private fun fetch(restaurantId: Int, date: LocalDate) =
        fetchFromPostgres(restaurantId, date)

    fun fetchFromUsp(restaurantId: Int): Set<RestaurantItem> {
        val items = uspRestaurantItemRepository.fetch(restaurantId)
        postgresRestaurantItemRepository.put(items)
        return items
    }

    private fun fetchFromPostgres(restaurantId: Int, date: LocalDate) =
        postgresRestaurantItemRepository.get(restaurantId, date)
}
