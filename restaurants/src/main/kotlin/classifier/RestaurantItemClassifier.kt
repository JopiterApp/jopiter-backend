package app.jopiter.restaurants.classifier

import app.jopiter.restaurants.model.ClassifiedRestaurantItem
import app.jopiter.restaurants.model.DessertFoodGroup
import app.jopiter.restaurants.model.DessertItem
import app.jopiter.restaurants.model.DessertPreparation
import app.jopiter.restaurants.model.ProteinFoodGroup
import app.jopiter.restaurants.model.ProteinPreparation
import app.jopiter.restaurants.model.ProteinItem
import app.jopiter.restaurants.model.RestaurantItem
import app.jopiter.restaurants.model.VegetarianFoodGroup
import app.jopiter.restaurants.model.VegetarianItem
import app.jopiter.restaurants.model.VegetarianPreparation
import org.springframework.stereotype.Component


@Component
class RestaurantItemClassifier(
  private val proteinClassifier: ProteinClassifier,
  private val vegetarianClassifier: VegetarianClassifier,
  private val dessertClassifier: DessertClassifier
) {

  fun classify(restaurantItem: RestaurantItem): ClassifiedRestaurantItem {
    return ClassifiedRestaurantItem(
      restaurantItem.restaurantId,
      restaurantItem.date,
      restaurantItem.period,
      restaurantItem.calories,
      restaurantItem.mainItem?.let { classifyProtein(it) },
      restaurantItem.vegetarianItem?.let { classifyVegetarian(it) },
      restaurantItem.dessertItem?.let { classifyDessert(it) },
      restaurantItem.mundaneItems,
      restaurantItem.unparsedMenu,
      restaurantItem.restaurantName
    )
  }

  fun classifyProtein(item: String): ProteinItem {
    val classification = proteinClassifier.classify(item)
    return ProteinItem(item, ProteinFoodGroup.find(classification.foodGroup), ProteinPreparation.find(classification.preparation))
  }

  fun classifyVegetarian(item: String): VegetarianItem {
    val classification = vegetarianClassifier.classify(item)
    return VegetarianItem(item, VegetarianFoodGroup.find(classification.foodGroup), VegetarianPreparation.find(classification.preparation))
  }

  fun classifyDessert(item: String): DessertItem {
    val classification = dessertClassifier.classify(item)
    return DessertItem(item, DessertFoodGroup.find(classification.foodGroup), DessertPreparation.find(classification.preparation))
  }

}