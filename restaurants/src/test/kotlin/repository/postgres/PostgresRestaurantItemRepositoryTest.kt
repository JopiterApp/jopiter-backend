package app.jopiter.restaurants.repository.postgres

import app.jopiter.restaurants.model.Period
import app.jopiter.restaurants.model.RestaurantItem
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.JdbcTestContainerExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate

class PostgresRestaurantItemRepositoryTest : FunSpec({

  val postgresContainer = PostgreSQLContainer<Nothing>("postgres")
  val datasource = install(JdbcTestContainerExtension(postgresContainer))
  val flyway = Flyway.configure().dataSource(datasource).load()

  val database = Database.connect(datasource)
  val target = PostgresRestaurantItemRepository(database)

  beforeSpec {
    flyway.clean()
    flyway.migrate()
  }

  test("Inserts into the database") {
    val item = RestaurantItem(
      restaurantId = 1,
      restaurantName = "foo",
      date = LocalDate.now(),
      period = Period.Lunch,
      calories = 10,
      mainItem = "main",
      vegetarianItem = "vegetarian",
      dessertItem = "dessert",
      mundaneItems = emptyList(),
      unparsedMenu = ""
    )

    target.put(item)

    database.sequenceOf(RestaurantItems).toList().shouldBeSingleton {
      it.restaurantId shouldBe 1
      it.restaurantName shouldBe "foo"
      it.date shouldBe LocalDate.now()
      it.period shouldBe Period.Lunch
      it.calories shouldBe 10
      it.mainItem shouldBe "main"
      it.vegetarianItem shouldBe "vegetarian"
      it.dessertItem shouldBe "dessert"
      it.mundaneItems.shouldBeEmpty()
      it.unparsedMenu shouldBe ""
    }
  }
})
