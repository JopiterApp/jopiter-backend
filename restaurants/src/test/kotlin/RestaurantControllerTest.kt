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

import app.jopiter.jopiter
import app.jopiter.koinApp
import app.jopiter.restaurants.model.Campus
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.model.restaurantItemArb
import app.jopiter.restaurants.repository.RestaurantItemRepository
import com.github.kittinunf.fuel.coroutines.awaitObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import io.javalin.plugin.json.JavalinJackson.getObjectMapper
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.take
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate.of

class RestaurantControllerTest : FunSpec({
    val repository = mockk<RestaurantItemRepository>()
    koinApp.koin.declare(repository, override = true)
    
    val app = jopiter().start()
    val url = "http://localhost:${app.port()}/api/v1"

    test("Returns the restaurant list as a json") {
        val response: List<CampusSerial> = "$url/restaurants".httpGet().awaitObject(jacksonDeserializerOf())

        response shouldHaveSize Campus.values().size
        response.forAll { ser ->
            val campus = Campus.values().single { ser.campusName == it.campusName }
            ser.restaurants shouldHaveSize campus.restaurants.size
            ser.restaurants.forAll { resSer ->
                campus.restaurants.shouldExist { resSer.restaurantName == it.restaurantName && resSer.id == it.id }
            }
        }
    }

    test("Returns what the restaurant repository returns") {
        val restaurantItems = restaurantItemArb.take(100).toSet()
        every { repository.get(1, setOf(of(1998, 2, 9), of(1998, 2, 21))) } returns restaurantItems

        val response: List<RestaurantItem> = "$url/restaurants/items".httpGet(
            listOf("restaurantId" to 1, "date" to "1998-02-09", "date" to "1998-02-21")
        ).awaitObject(jacksonDeserializerOf(getObjectMapper()))

        response shouldBe restaurantItems
    }

    afterSpec { app.stop() }
})

private data class CampusSerial(
    val campusName: String,
    val restaurants: List<RestaurantSerial>
)

private data class RestaurantSerial(
    val restaurantName: String,
    val id: Int
)
