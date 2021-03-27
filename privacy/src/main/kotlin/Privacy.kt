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

package app.jopiter.privacy

import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiResponse
import org.koin.dsl.module

val privacyModule = module {
    single { PrivacyController() }
}

class PrivacyController {
    private val privacyHtml = PrivacyController::class.java.classLoader.getResource("privacy.html")!!.readText()

    @OpenApi(
        summary = "Privacy Policy",
        description = "Jopiter App privacy policy",
        tags = ["privacy"],
        responses = [
            OpenApiResponse(status = "200", description = "Returns the privacy policy file")
        ]
    )
    fun get(context: Context) {
        context.html(privacyHtml)
    }
}
