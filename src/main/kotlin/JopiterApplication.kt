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

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@PropertySources(
    PropertySource("application-restaurants.properties")
)
class JopiterApplication {
    @Bean fun objectMapper() = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())

    @Bean
    fun openApi() = OpenAPI()
        .info(Info()
            .title("Jopiter Backend")
            .description("API specification for the Jopiter App")
            .version("1.0.0")
            .license(License()
                .name("AGPL-3.0 License")
                .url("https://github.com/JopiterApp/jopiter-backend/blob/master/LICENSE")
            )
        )
      .addServersItem(
        Server().description("Production").url("https://v2.backend.jopiter.app")
      )
}

fun main() {
    runApplication<JopiterApplication>()
}
