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

package repository.usp

import app.jopiter.restaurants.model.Period.Dinner
import app.jopiter.restaurants.model.Period.Lunch
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.repository.usp.Menu
import app.jopiter.restaurants.repository.usp.MenuParser
import app.jopiter.restaurants.repository.usp.USPRestaurantItemRepository
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.unmockkAll
import org.mockserver.integration.ClientAndServer.startClientAndServer
import org.mockserver.mock.OpenAPIExpectation.openAPIExpectation
import org.mockserver.model.Delay.seconds
import org.mockserver.model.HttpRequest.request
import java.time.LocalDate.of

private val uspRestaurantSpecUrl = "https://raw.githubusercontent.com/JopiterApp/USP-Restaurant-API/main/openapi.yaml"
private val specification = openAPIExpectation(uspRestaurantSpecUrl)

class USPRestaurantItemRepositoryTest : ShouldSpec({

    val mockServer = startClientAndServer().apply { upsert(specification) }

    val target = USPRestaurantItemRepository("http://localhost:${mockServer.localPort}", dummyParser, "hash")

    should("Parse a menu using the right parser") {
        val parsed = target.fetch(6)

        parsed shouldBe setOf(
            RestaurantItem(6, of(2020, 3, 9), Lunch, 1219, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 9), Dinner, 1056, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 13), Lunch, 1145, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 13), Dinner, 895, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 14), Lunch, 1236, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 14), Dinner, 0, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 15), Lunch, 0, "foo", "bar", "baz", emptyList(), "", "Central"),
            RestaurantItem(6, of(2020, 3, 15), Dinner, 0, "foo", "bar", "baz", emptyList(), "", "Central"),
        )
    }

    should("Timeout after 2s") {
        val delayedExpectations = mockServer.retrieveActiveExpectations(request()).map { it.apply{ httpResponse.withDelay(seconds(30)) } }
        mockServer.upsert(*delayedExpectations.toTypedArray())

        target.fetch(6).shouldBeEmpty()
    }

    afterSpec { mockServer.stop(); unmockkAll() }

})

private val dummyParser = mapOf(
    6 to MenuParser { Menu("foo", "bar", "baz", emptyList(), "") }
)