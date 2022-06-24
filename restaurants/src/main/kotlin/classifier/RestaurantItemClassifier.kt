package app.jopiter.restaurants.classifier

import app.jopiter.restaurants.model.ClassifiedRestaurantItem
import app.jopiter.restaurants.model.FoodGroup
import app.jopiter.restaurants.model.Period
import app.jopiter.restaurants.model.Preparation
import app.jopiter.restaurants.model.ProteinItem
import app.jopiter.restaurants.model.RestaurantItem
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class RestaurantItemClassifier(
  private val proteinClassifier: ProteinClassifier
) {

  fun classify(restaurantItem: RestaurantItem): ClassifiedRestaurantItem {
    return ClassifiedRestaurantItem(
      restaurantItem.restaurantId,
      restaurantItem.date,
      restaurantItem.period,
      restaurantItem.calories,
      restaurantItem.mainItem?.let { classifyProtein(it) },
      restaurantItem.vegetarianItem,
      restaurantItem.dessertItem,
      restaurantItem.mundaneItems,
      restaurantItem.unparsedMenu,
      restaurantItem.restaurantName
    )
  }

  private fun classifyProtein(item: String): ProteinItem {
    val proteinClassification = proteinClassifier.classify(item)
    return ProteinItem(item, FoodGroup.find(proteinClassification.foodGroup), Preparation.find(proteinClassification.preparation))
  }

}