package app.jopiter.restaurants.repository.postgres

import app.jopiter.restaurants.model.Period
import app.jopiter.restaurants.model.RestaurantItem
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.map
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.entity.toSet
import org.ktorm.entity.update
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.ktorm.support.postgresql.insertOrUpdate
import org.ktorm.support.postgresql.textArray
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class PostgresRestaurantItemRepository(
  private val database: Database,
) {

  private val logger = LoggerFactory.getLogger(this::class.java)

  private val restaurantItems = database.sequenceOf(RestaurantItems)

  fun put(items: Collection<RestaurantItem>) = items.forEach(::put)

  fun put(restaurantItem: RestaurantItem) {
    val items = get(restaurantItem.restaurantId, restaurantItem.date)


    if(get(restaurantItem.restaurantId, restaurantItem.date).isEmpty()) {
      restaurantItems.add(restaurantItem.toEntity())
    } else {
      restaurantItems.update(restaurantItem.toEntity())
    }
  }

  fun get(restaurantId: Int, date: LocalDate): Set<RestaurantItem> = restaurantItems
    .filter { it.date eq date }
    .filter { it.restaurantId eq restaurantId }
    .map { it.toItem() }
    .toSet()
}

object RestaurantItems : Table<RestaurantItemEntity>("restaurant_item") {
  val restaurantId = int("restaurant_id").primaryKey().bindTo { it.restaurantId }
  val restaurantName = varchar("restaurant_name").bindTo { it.restaurantName }
  val date = date("date").primaryKey().bindTo { it.date }
  val period = enum<Period>("period").primaryKey().bindTo { it.period }
  val calories = int("calories").bindTo { it.calories }
  val mainItem = varchar("main_item").bindTo { it.mainItem }
  val vegetarianItem = varchar("vegetarian_item").bindTo { it.vegetarianItem }
  val dessertItem = varchar("dessert_item").bindTo { it.dessertItem }
  val mundaneItems = textArray("mundane_items").bindTo { it.mundaneItems }
  val unparsedMenu = varchar("unparsed_menu").bindTo { it.unparsedMenu }
}

interface RestaurantItemEntity : Entity<RestaurantItemEntity> {
  var restaurantId: Int
  var restaurantName: String
  var date: LocalDate
  var period: Period
  var calories: Int?
  var mainItem: String?
  var vegetarianItem: String?
  var dessertItem: String?
  var mundaneItems: Array<String?>
  var unparsedMenu: String

  companion object : Entity.Factory<RestaurantItemEntity>()
}

private fun RestaurantItem.toEntity() = let {
  RestaurantItemEntity {
    restaurantId = it.restaurantId
    restaurantName = it.restaurantName
    date = it.date
    period = it.period
    calories = it.calories
    mainItem = it.mainItem
    vegetarianItem = it.vegetarianItem
    dessertItem = it.dessertItem
    mundaneItems = it.mundaneItems.toTypedArray()
    unparsedMenu = it.unparsedMenu
  }
}

private fun RestaurantItemEntity.toItem() = let {
  RestaurantItem(
    restaurantId = it.restaurantId,
    restaurantName = it.restaurantName,
    date = it.date,
    period = it.period,
    calories = it.calories,
    mainItem = it.mainItem,
    vegetarianItem = it.vegetarianItem,
    dessertItem = it.dessertItem,
    mundaneItems = it.mundaneItems.filterNotNull(),
    unparsedMenu = it.unparsedMenu
  )
}
