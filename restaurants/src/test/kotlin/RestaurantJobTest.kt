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

import app.jopiter.restaurants.model.Restaurant
import app.jopiter.restaurants.repository.RestaurantItemRepository
import app.jopiter.restaurants.repository.postgres.PostgresRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.USPRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.parsers
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.verifyAll
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate.now

class RestaurantJobTest : ShouldSpec({

    val postgresContainer = PostgreSQLContainer<Nothing>("postgres")
    val datasource = install(JdbcTestContainerExtension(postgresContainer))
    val flyway = Flyway.configure().dataSource(datasource).load()

    val database = Database.connect(datasource)

    beforeSpec {
        flyway.clean()
        flyway.migrate()
    }

    val postgresRepository = PostgresRestaurantItemRepository(database)
    val uspRepository = USPRestaurantItemRepository("https://uspdigital.usp.br/rucard/servicos", parsers, "596df9effde6f877717b4e81fdb2ca9f")
    val restaurantItemRepository = RestaurantItemRepository(uspRepository, postgresRepository)

    context("Attempt to get every parseable restaurant menu for the current day") {
        val target = RestaurantJob(RestaurantItemRepository(uspRepository, postgresRepository))
        target.run()

        Restaurant.values().sortedBy { it.id }.forEach {
            should("Not throw an error when parsing ${it.id}") {
                val response = restaurantItemRepository.get(it.id, setOf(now()))
                if(response.isEmpty()) {
                    // Apparently, Crhea was closed back in 2020-03, but still responds and is still listed
                    it shouldBe Restaurant.Crhea
                }
                response.take(2).forEach {
                    println("Example(${it.restaurantId}, \"${it.unparsedMenu.replace("\n", "\\n")}\", \"${it.mainItem}\", \"${it.vegetarianItem}\", \"${it.dessertItem}\", \"mudane\")")
                }
            }
        }
    }
})
