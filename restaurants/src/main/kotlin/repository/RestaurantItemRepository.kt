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
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Repository
import org.threeten.extra.YearWeek
import java.time.Duration.ofMinutes
import java.time.LocalDate

private typealias RestaurantKey = Pair<Int, LocalDate>

@Repository
class RestaurantItemRepository(
    private val uspRestaurantItemRepository: USPRestaurantItemRepository,
    private val postgresRestaurantItemRepository: PostgresRestaurantItemRepository
) {
    private val cache = Caffeine
        .newBuilder()
        .expireAfterWrite(ofMinutes(15))
        .build<RestaurantKey, Set<RestaurantItem>> { fetch(it.first, it.second) }

    fun get(restaurantId: Int, dates: Set<LocalDate>) =
        dates.flatMap { cache.get(restaurantId to it).orEmpty() }.toSet()

    private fun fetch(restaurantId: Int, date: LocalDate) =
        fetchFromUsp(restaurantId, date).ifEmpty { postgresRestaurantItemRepository.get(restaurantId, date) }

    private fun fetchFromUsp(restaurantId: Int, date: LocalDate): Set<RestaurantItem> {
        val items = if (YearWeek.from(date) == YearWeek.now()) {
            uspRestaurantItemRepository.fetch(restaurantId)
        } else emptySet()

        val (requested, extra) = items.partition { it.date == date }

        cache.putAll(extra.groupBy { it.restaurantId to it.date }.mapValues { it.value.toSet() })
        postgresRestaurantItemRepository.put(items)

        return requested.toSet()
    }
}
