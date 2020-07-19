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

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.string.shouldContain
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication

class StaticResourcesTests : ShouldSpec({
   context("/privacy") {
       should("return the privacy file") {
           withTestApplication {
               Server().prepare(this.application)
               handleRequest(Get, "/privacy").response.content shouldContain "Privacy Policy"
           }
       }
   }
})
