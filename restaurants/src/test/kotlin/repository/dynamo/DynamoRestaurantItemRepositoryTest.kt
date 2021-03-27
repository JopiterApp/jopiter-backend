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

package repository.dynamo

import app.jopiter.restaurants.model.Period.Dinner
import app.jopiter.restaurants.model.Period.Lunch
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.model.restaurantItemArb
import app.jopiter.restaurants.repository.dynamo.DynamoRestaurantItemRepository
import app.jopiter.restaurants.repository.dynamo.RestaurantItemEntity
import app.jopiter.restaurants.repository.dynamo.dynamoDbClient
import app.jopiter.restaurants.repository.dynamo.toRestaurantEntity
import app.jopiter.restaurants.repository.dynamo.toRestaurantItem
import br.com.colman.dynamodb.DynamoDbEnhancedClient
import br.com.colman.dynamodb.putItems
import br.com.colman.dynamodb.table
import io.kotest.core.spec.IsolationMode.InstancePerTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.take
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

class DynamoRestaurantItemRepositoryTest : ShouldSpec({
    val client = DynamoDbEnhancedClient(dynamoDbClient)
    val table = client.table<RestaurantItemEntity>("restaurants")
    val target = DynamoRestaurantItemRepository(client)

    should("Retrieve items present in the table") {
        val items = table.createItems()

        val result = items.flatMap { target.get(it.restaurantId, it.date) }

        result shouldContainExactlyInAnyOrder items
    }

    should("Retrieve items only from particular dates") {
        val items = table.createItems(10_000)

        val result = target.get(1, items[0].date)

        val itemsFromDate = items.filter { it.date == items[0].date && it.restaurantId == 1 }

        result shouldContainExactlyInAnyOrder itemsFromDate
    }

    should("Retrieve items from both periods for a given restaurant") {
        val item1 = restaurantItemArb.next()
        val item2 = item1.copy(period = if(item1.period == Lunch) Dinner else Lunch)
        table.putItems(item1.toRestaurantEntity(), item2.toRestaurantEntity())

        target.get(item1.restaurantId, item1.date) shouldBe setOf(item1, item2)
    }

    should("Save items to table") {
        val items = restaurantItemArb.take(2).toSet()

        target.put(items)

        table.scan().items().map { it.toRestaurantItem() } shouldContainExactlyInAnyOrder items
    }

    beforeTest { client.createTable() }
    isolationMode = InstancePerTest
})

private fun DynamoDbTable<RestaurantItemEntity>.createItems(amount: Int = 1_000): List<RestaurantItem> {
    val items = restaurantItemArb.take(amount).toList()
    val databaseItems = items.distinctBy { it.restaurantId to it.date }.map { it.toRestaurantEntity() }

    putItems(databaseItems)

    return items
}

private fun DynamoDbEnhancedClient.createTable() = table<RestaurantItemEntity>("restaurants").createTable()

