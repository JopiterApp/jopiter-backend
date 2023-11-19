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

import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.shouldBe

@Tags("Daily")
class RestaurantTest : FunSpec({

    context("Restaurants model reflects what USP returns") {
        val restaurantGroups = fetchRestaurantGroupsFromUSP()

        test("Should have same number of campi") {
            Campus.values() shouldBeSameSizeAs restaurantGroups
        }

        test("Should have a 1 to 1 mapping between USP's response and our model") {
            val allRestaurants = restaurantGroups.flatMap { it.restaurants }
            allRestaurants.forAll { (alias, id) ->
                Restaurant.getById(id).restaurantName shouldBe alias
            }
        }
    }

    test("Should return restaurant by id") {
        val restaurant = Restaurant.values().random()
        Restaurant.getById(restaurant.id) shouldBe restaurant
    }
})

private fun fetchRestaurantGroupsFromUSP() =
    "https://uspdigital.usp.br/rucard/servicos/restaurants".httpPost()
        .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
        .body("hash=596df9effde6f877717b4e81fdb2ca9f")
        .responseObject<List<RestaurantGroup>>().third.get()

data class RestaurantGroup(val name: String, val restaurants: List<IndividualRestaurant>)
data class IndividualRestaurant(val alias: String, val id: Int)
