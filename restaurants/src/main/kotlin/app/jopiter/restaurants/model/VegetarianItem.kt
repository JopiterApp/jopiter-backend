package app.jopiter.restaurants.model

data class VegetarianItem(
  val item: String,
  val foodGroup: VegetarianFoodGroup,
  val preparation: VegetarianPreparation
)

enum class VegetarianFoodGroup(val foodGroup: String, val score: Double) {
  CerealMilho("cereal (milho)", 1.0),
  Tuberculo("tubérculo", 0.5),
  Legumes("legumes", 1.0),
  Folhosos("folhosos", 1.0),
  Leguminosa("leguminosa", 2.0),
  PTS("PTS", 2.0),
  Soja("soja", 2.0),
  MassaTorta("massa/torta", 0.5),
  Ovos("ovos", 2.0),
  Queijo("queijo", 1.0);

  companion object {
    fun find(foodGroup: String) = values().first { it.foodGroup == foodGroup }
  }
}

enum class VegetarianPreparation(val preparation: String, val score: Int) {
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
