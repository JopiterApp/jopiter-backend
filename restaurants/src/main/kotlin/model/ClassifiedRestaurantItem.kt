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
  val vegetarianItem: String?,
  val dessertItem: String?,
  val mundaneItems: List<String>,
  val unparsedMenu: String,
  val restaurantName: String
)

data class ProteinItem(
  val item: String,
  val foodGroup: FoodGroup,
  val preparation: Preparation
)

enum class FoodGroup(val foodGroup: String, val score: Int) {
  Ave("ave", 2),
  Bovina("bovina", 1),
  Ovo("ovo", 2),
  Peixe("peixe", 2),
  Suina("suina", 1),
  Processada("processada", 0);

  companion object {
    fun find(foodGroup: String) = values().first { it.foodGroup == foodGroup }
  }
}

enum class Preparation(val preparation: String, val score: Int) {
  AoMolhoGorduroso("ao molho gorduroso", 1),
  Assado("assado", 2),
  Cozido("cozido", 2),
  Frito("frito", 0),
  GrelhadoRefogado("grelhado/refogado", 2),
  AoMolhoLeve("ao molho leve", 2);

  companion object {
    fun find(preparation: String) = values().first { it.preparation == preparation }
  }
}