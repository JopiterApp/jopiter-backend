# Restaurants

This module is responsible for fetching restaurant items from USP (through [USP Restaurant API][1]) and persist them in a DynamoDB table ([restaurants][2]), while also providing an API for the retrieval of these items.

## Fetching from USP

We trust the [USP Restaurant API][1] definitions, and with them the [USPRestaurantItemRepository][3] is able to fetch items from USP's api.

### Parsing the menu

It's important for this application to parse all values, as clients are going to expect a smarter output instead of just the menus (otherwise, why would this module be necessary?). For that we have the [MenuParsers][4]. We try with our best effort to parse the menus, however there is some difficulty as restaurants won't always report items in the same way.

A future point of improvement is being able to parse menus without such a brittle and forced solution.

## Saving to DynamoDB

Every item obtained from USP is piped into a DynamoDB item. This enables us to respond to clients even when USP's servers are down (which happen quite often). The DynamoDB table [restaurants][2] keep the data. You can look at the schema in the terraform file.

## Cache

When an item is successfully obtained, it's first persisted on the DynamoDB table (see above), and then kept in an in-memory cache. This cache allow us to quickly respond users while keeping costs down.

It's a good idea to use a cache here, as the data that clients want is usually the menus for the current week, making cache hits very likely.

## Restaurant Job

In order to have all records available - even for restaurants that aren't accessed regularly - we continuously try to get all restaurants from USP, using a fixed rate schedule in [RestaurantJob class][5].

This will both refresh our current cache and save any new menus to our persistent storage.



[1]: https://github.com/JopiterApp/USP-Restaurant-API
[2]: ../terraform/restaurants.tf
[3]: src/main/kotlin/repository/usp/USPRestaurantItemRepository.kt
[4]: src/main/kotlin/repository/usp/MenuParser.kt
[5]: src/main/kotlin/RestaurantJob.kt