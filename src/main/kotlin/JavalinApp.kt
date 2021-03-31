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

package app.jopiter

import app.jopiter.privacy.PrivacyController
import app.jopiter.restaurants.RestaurantController
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.JavalinConfig
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.swagger.v3.oas.models.info.Info

@Suppress("MagicNumber")
fun main() {
    jopiter().start(5000)
}

fun jopiter(): Javalin {
    val app = Javalin.create { it.registerOpenApi() }
    koinApp.koin.get<Router>().register(app)
    JavalinJackson.configure(ObjectMapper().registerModule(JavaTimeModule()).registerKotlinModule())

    return app
}

private fun JavalinConfig.registerOpenApi() {
    val applicationInfo: Info = Info()
        .version("1.0")
        .title("Jopiter Backend")
        .description("API specification for the Jopiter App")
    registerPlugin(OpenApiPlugin(
        OpenApiOptions(info = applicationInfo).path("/api").reDoc(ReDocOptions("/api/docs"))
    ))
}


class Router(
    private val privacyController: PrivacyController,
    private val restaurantController: RestaurantController
) {
    fun register(app: Javalin) {
        app.routes {
            get("/health") { it.result("OK") }

            path("/privacy") {
                get(privacyController::get)
            }
            path("/api/v1") {
                path("/restaurants") {
                    get(restaurantController::list)
                    get("/items", restaurantController::items)
                }
            }
        }
    }
}
