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

import app.jopiter.jopiter
import com.github.kittinunf.fuel.httpGet
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.string.shouldContain

class PrivacyTest : ShouldSpec({
    val app = jopiter().start(0)

    should("Return the privacy file from the right path") {
        "http://localhost:${app.port()}/privacy".httpGet().responseString().third.get() shouldContain "Leonardo Colman Lopes built the Jopiter app"
    }
})