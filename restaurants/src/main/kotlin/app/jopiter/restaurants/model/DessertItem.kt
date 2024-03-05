package app.jopiter.restaurants.model

data class DessertItem(
  val item: String,
  val foodGroup: DessertFoodGroup,
  val preparation: DessertPreparation
)

enum class DessertFoodGroup(val foodGroup: String, val score: Int) {
  Doce("doce", 0),
  Fruta("fruta", 2);

  companion object {
    fun find(foodGroup: String) = values().first { it.foodGroup == foodGroup }
  }
}

enum class DessertPreparation(val preparation: String, val score: Int) {
  UltraProcessado("ultraprocessado", 0),
  NaoUltraProcessado("", 2);

  companion object {
    fun find(preparation: String) = values().first { it.preparation == preparation }
  }
}
