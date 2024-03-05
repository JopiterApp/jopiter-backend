package app.jopiter.restaurants

import app.jopiter.restaurants.model.Restaurant
import app.jopiter.restaurants.repository.RestaurantItemRepository
import app.jopiter.restaurants.repository.postgres.PostgresRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.USPRestaurantItemRepository
import app.jopiter.restaurants.repository.usp.parsers
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate

class RestaurantJobTest : ShouldSpec({

  val postgresContainer = PostgreSQLContainer<Nothing>("postgres")
  val datasource = install(JdbcTestContainerExtension(postgresContainer))
  val flyway = Flyway.configure().cleanDisabled(false).dataSource(datasource).load()

  val database = Database.connect(datasource)

  beforeSpec {
    flyway.clean()
    flyway.migrate()
  }

  val postgresRepository = PostgresRestaurantItemRepository(database)
  val uspRepository = USPRestaurantItemRepository("https://uspdigital.usp.br/rucard/servicos", parsers, "596df9effde6f877717b4e81fdb2ca9f")
  val restaurantItemRepository = RestaurantItemRepository(uspRepository, postgresRepository)
  val target = RestaurantJob(restaurantItemRepository)

  context("Attempt to get every parseable restaurant menu for the current day") {
    target.run()

    Restaurant.entries.sortedBy { it.id }.forEach {
      should("Not throw an error when parsing ${it.id}") {
        val response = restaurantItemRepository.get(it.id, setOf(LocalDate.now()))
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
