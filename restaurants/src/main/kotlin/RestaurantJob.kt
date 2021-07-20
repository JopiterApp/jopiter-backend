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

package app.jopiter.restaurants

import app.jopiter.restaurants.model.Restaurant
import app.jopiter.restaurants.repository.RestaurantItemRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate.now
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit.MINUTES

@Component
class RestaurantJob(
    private val repository: RestaurantItemRepository
) {

    @Scheduled(fixedRate = 15_000)
    fun run() = Restaurant.values().forEach { repository.get(it.id, setOf(now())) }
}
