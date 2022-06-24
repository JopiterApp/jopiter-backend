package app.jopiter.restaurants.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.stereotype.Component
import java.time.LocalDate


data class ClassifiedRestaurantItem(
  val restaurantId: Int,
  @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING) val date: LocalDate,
  val period: Period,
  val calories: Int?,
  val mainItem: ProteinItem?,
  val vegetarianItem: VegetarianItem?,
  val dessertItem: DessertItem?,
  val mundaneItems: List<String>,
  val unparsedMenu: String,
  val restaurantName: String
)
