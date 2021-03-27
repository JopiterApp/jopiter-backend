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
import io.kotest.assertions.assertSoftly
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.lang.System.getenv


@Tags("Daily")
class RestaurantTest : FunSpec({

    beforeSpec { getenv("USP_RESTAURANT_HASH") shouldNotBe null }

    test("Restaurants model reflects what USP returns") {
        val restaurantsReturnedByUSP = "https://uspdigital.usp.br/rucard/servicos/restaurants".httpPost()
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .body("hash=${getenv("USP_RESTAURANT_HASH")}")
            .responseObject<List<RestaurantGroup>>().third.get()

        with(restaurantsReturnedByUSP) {
            Campus.values() shouldHaveSize size
            
            forEach { group ->
                val campus = Campus.values().single { it.campusName == group.name }
                group.restaurants.forAll { restaurant ->
                    campus.restaurants.shouldExist { it.id == restaurant.id && it.restaurantName == restaurant.alias }
                }
            }
                        
        }
    }
    
    test("Should return restaurant by id") {
        val restaurant = Restaurant.values().random()
        Restaurant.getById(restaurant.id) shouldBe restaurant
    }
})

data class RestaurantGroup(val name: String, val restaurants: List<IndividualRestaurant>)
data class IndividualRestaurant(val alias: String, val id: Int)


