/*
 * Jopiter App
 * Copyright (C) 2020 Leonardo Colman Lopes
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

import io.kotless.dsl.ktor.Kotless
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

class Server : Kotless() {
    
    override fun prepare(app: Application) {
        app.routing { 
            get("/privacy") {
                call.respondHtmlFile("privacy.html")
            }
        }
    }
}

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
private suspend inline fun ApplicationCall.respondHtmlFile(resource: String) {
    respondText(Server::class.java.classLoader.getResource(resource).readText(), ContentType.Text.Html)
}

