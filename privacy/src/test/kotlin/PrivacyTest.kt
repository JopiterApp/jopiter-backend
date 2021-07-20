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

import com.github.kittinunf.fuel.httpGet
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(classes = [PrivacyController::class])
@EnableAutoConfiguration
class PrivacyTest(@LocalServerPort val port: Int) : ShouldSpec({

    extension(SpringExtension)

    should("Return the privacy file from the right path") {
        "http://localhost:$port/privacy".httpGet().responseString().third.get() shouldContain "Leonardo Colman Lopes built the Jopiter app"
    }
})
