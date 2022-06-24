package app.jopiter.restaurants.classifier

import app.jopiter.restaurants.model.ClassifiedRestaurantItem
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
  private val vegetarianClassifier: VegetarianClassifier
) {

  fun classify(restaurantItem: RestaurantItem): ClassifiedRestaurantItem {
    return ClassifiedRestaurantItem(
      restaurantItem.restaurantId,
      restaurantItem.date,
      restaurantItem.period,
      restaurantItem.calories,
      restaurantItem.mainItem?.let { classifyProtein(it) },
      restaurantItem.vegetarianItem?.let { classifyVegetarian(it) },
      restaurantItem.dessertItem,
      restaurantItem.mundaneItems,
      restaurantItem.unparsedMenu,
      restaurantItem.restaurantName
    )
  }

  private fun classifyProtein(item: String): ProteinItem {
    val proteinClassification = proteinClassifier.classify(item)
    return ProteinItem(item, ProteinFoodGroup.find(proteinClassification.foodGroup), ProteinPreparation.find(proteinClassification.preparation))
  }

  private fun classifyVegetarian(item: String): VegetarianItem {
    val vegetarianClassification = vegetarianClassifier.classify(item)
    return VegetarianItem(item, VegetarianFoodGroup.find(vegetarianClassification.foodGroup), VegetarianPreparation.find(vegetarianClassification.preparation))
  }

}