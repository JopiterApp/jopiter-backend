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

import app.jopiter.restaurants.model.Campus
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.repository.RestaurantItemRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("\${api.base.path}/restaurants")
class RestaurantController(
    private val restaurantItemRepository: RestaurantItemRepository,
) {

    @Operation(
        summary = "List all restaurants and their campi",
        description = "List all restaurants that are available, including to which campus it belongs",
        tags = ["restaurant"],
        responses = [
            ApiResponse(responseCode = "200", content = [
                Content(array = ArraySchema(schema = Schema(implementation = Campus::class)),
                    mediaType = "application/json")
            ])
        ]
    )
    @GetMapping
    fun list() = Campus.values().toList()

    @Operation(
        summary = "List Items",
        description = "Retrieves all items for the chosen dates and restaurant",
        tags = ["restaurant"],

        parameters = [
            Parameter(
                name = "restaurantId", description = "The restaurant ID as defined by /restaurants", required = true
            ),
            Parameter(
                name = "date", description = "The dates you want to fetch items for. ISO_LOCAL_DATE format (yyyy-MM-dd)"
            )
        ],

        responses = [
            ApiResponse(responseCode = "200", content = [
                Content(array = ArraySchema(schema = Schema(implementation = RestaurantItem::class)),
                    mediaType = "application/json")
            ])
        ]
    )
    @GetMapping("/items")
    fun items(
        @RequestParam("restaurantId") restaurantId: Int,
        @RequestParam("date") @DateTimeFormat(iso = DATE) dates: Set<LocalDate>,
    ) = restaurantItemRepository.get(restaurantId, dates)
}
