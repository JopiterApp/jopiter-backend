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

@file:Suppress("MaxLineLength")
package app.jopiter.restaurants.repository.dynamo

import app.jopiter.restaurants.model.Period.valueOf
import app.jopiter.restaurants.model.RestaurantItem
import br.com.colman.dynamodb.Key
import br.com.colman.dynamodb.putItems
import br.com.colman.dynamodb.table
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.sortBeginsWith
import java.time.LocalDate
import java.time.LocalDate.parse

@Configuration class DynamoClientConfiguration {
    @Bean fun dynamoDbEnhancedClient() = DynamoDbEnhancedClient.create()
}

@Repository
class DynamoRestaurantItemRepository(dynamoDb: DynamoDbEnhancedClient) {

    private val restaurantsTable = dynamoDb.table<RestaurantItemEntity>("restaurants")

    fun get(restaurantId: Int, date: LocalDate) = restaurantsTable.query(
        sortBeginsWith(Key(restaurantId, "$date"))
    ).items().map { it.toRestaurantItem() }.toSet()

    fun put(items: Set<RestaurantItem>) = restaurantsTable.putItems(items.map { it.toRestaurantEntity() })

}

@DynamoDbBean
data class RestaurantItemEntity(
    @get:DynamoDbPartitionKey var restaurantId: Int,
    var restaurantName: String,
    var isoDate: String,
    var period: String,
    var calories: Long?,
    var mainItem: String?,
    var vegetarianItem: String?,
    var dessertItem: String?,
    var mundaneItems: List<String>,
    var unparsedMenu: String
) {
    @Suppress("unused")
    @get:DynamoDbSortKey
    var sortKey: String = "$isoDate/$period"
}

fun RestaurantItemEntity.toRestaurantItem() = RestaurantItem(restaurantId, parse(isoDate), valueOf(period), calories, mainItem, vegetarianItem, dessertItem, mundaneItems, unparsedMenu, restaurantName)

fun RestaurantItem.toRestaurantEntity() = RestaurantItemEntity(restaurantId, restaurantName, "$date", "$period", calories, mainItem, vegetarianItem, dessertItem, mundaneItems, unparsedMenu)
