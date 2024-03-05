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

package app.jopiter.restaurants.repository

import app.jopiter.restaurants.model.Period.Lunch
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.repository.postgres.PostgresRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.USPRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.parsers
import io.kotest.core.extensions.install
import io.kotest.core.spec.IsolationMode.InstancePerTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.extensions.time.ConstantNowTestListener
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.testcontainers.containers.PostgreSQLContainer
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalDate
import java.time.LocalDate.now

class RestaurantItemRepositoryIntegrationTest : ShouldSpec({
  val postgres = install(JdbcTestContainerExtension(PostgreSQLContainer("postgres:16")))
  val flyway = Flyway.configure().cleanDisabled(false).dataSource(postgres).load()

  beforeSpec {
    flyway.clean()
    flyway.migrate()
  }

  val postgresRepository = PostgresRestaurantItemRepository(Database.connect(postgres))

  val uspRepository = USPRestaurantItemRepository(
    "https://uspdigital.usp.br/rucard/servicos",
    parsers,
    "596df9effde6f877717b4e81fdb2ca9f"
  )


  val target = RestaurantItemRepository(uspRepository, postgresRepository)

  should("Persist all items to the database") {
    val firstItemResult = target.get(13, setOf(LocalDate.of(2024, 3, 4)))
    val secondItemResult = target.get(13, setOf(LocalDate.of(2024, 3, 4)))

    firstItemResult shouldBe secondItemResult
  }

})

class RestaurantItemRepositoryTest : ShouldSpec({

  val uspRepository = mockk<USPRestaurantItemRepository>()
  val postgresRepository = mockk<PostgresRestaurantItemRepository>(relaxed = true)

  val target = RestaurantItemRepository(uspRepository, postgresRepository)

  listener(ConstantNowTestListener(now().with(WEDNESDAY)))


  should("Save any value fetched from USP to Postgres") {
    val today = now()
    val tomorrow = today.plusDays(1)

    every { uspRepository.fetch(1) } returns setOf(dummyRestaurantItem(today), dummyRestaurantItem(tomorrow))

    target.fetchFromUsp(1)

    verify(exactly = 1) { postgresRepository.put(setOf(dummyRestaurantItem(today), dummyRestaurantItem(tomorrow))) }
  }

  isolationMode = InstancePerTest

})

private fun dummyRestaurantItem(date: LocalDate) =
  RestaurantItem(1, date, Lunch, 0, "main", "veg", "des", emptyList(), "")
