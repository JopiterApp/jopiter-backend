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
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiParam
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import java.time.LocalDate.parse


class RestaurantController(
    private val restaurantItemRepository: RestaurantItemRepository
) {

    @OpenApi(
        summary = "List all restaurants and their campi",
        description = "List all restaurants that are available, including to which campus it belongs",
        tags = ["restaurant"],
        responses = [
            OpenApiResponse("200", [OpenApiContent(Campus::class, true)])
        ]
    )
    fun list(context: Context) {
        context.json(Campus.values())
    }

    @OpenApi(
        summary = "List Items",
        description = "Retrieves all items for the chosen dates and restaurant",
        tags = ["restaurant"],

        queryParams = [
            OpenApiParam(
                name = "restaurantId",
                type = Int::class,
                required = true,
                description = "The restaurant ID as defined by GET /restaurants"
            ),
            OpenApiParam(
                name = "date",
                type = String::class,
                required = true,
                description = "The dates you want to fetch items for. ISO_LOCAL_DATE format (yyyy-MM-dd)",
                isRepeatable = true
            )
        ],

        responses = [
            OpenApiResponse("200", [OpenApiContent(RestaurantItem::class, isArray = true)])
        ]
    )
    fun items(ctx: Context) {
        val restaurantIds = ctx.queryParam("restaurantId", "0")!!.toInt()
        val dates = ctx.queryParams("date").map { parse(it) }.toSet()
        ctx.json(restaurantItemRepository.get(restaurantIds, dates))
    }
}
