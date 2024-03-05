package app.jopiter.restaurants.model

data class ProteinItem(
  val item: String,
  val foodGroup: ProteinFoodGroup,
  val preparation: ProteinPreparation
)

enum class ProteinFoodGroup(val foodGroup: String, val score: Int) {
  Ave("ave", 2),
  Bovina("bovina", 1),
  Ovo("ovo", 2),
  Peixe("peixe", 2),
  Suina("suína", 1),
  Processada("processada", 0);

  companion object {
    fun find(foodGroup: String) = values().first { it.foodGroup == foodGroup }
  }
}

enum class ProteinPreparation(val preparation: String, val score: Int) {
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
