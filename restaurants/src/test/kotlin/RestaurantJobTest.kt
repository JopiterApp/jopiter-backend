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
import app.jopiter.restaurants.repository.usp.MenuParser
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyAll
import io.mockk.verifySequence
import java.time.LocalDate
import java.time.LocalDate.now
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class RestaurantJobTest : ShouldSpec({

    val repository = mockk<RestaurantItemRepository>(relaxed = true)

    should("Attempt to get every parseable restaurant menu for the current day") {
        val target = RestaurantJob(repository)
        target.run()

        verifyAll {
            Restaurant.values().forEach {
                repository.get(it.id, setOf(now()))
            }
        }
    }
})
