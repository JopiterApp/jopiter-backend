package app.jopiter.restaurants

import app.jopiter.restaurants.classifier.RestaurantItemClassifier
import app.jopiter.restaurants.model.DessertItem
import app.jopiter.restaurants.model.ProteinItem
import app.jopiter.restaurants.model.VegetarianItem
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.base.path}/classifier")
class ClassifierController(
  private val restaurantItemClassifier: RestaurantItemClassifier,
) {

  @Operation(
    summary = "Classify an item",
    description = "Attempts to classify a specific item in the categories [Protein, Vegetarian, Dessert], returning all guesses",
    tags = ["app/jopiter/restaurants/classifier"],

    parameters = [
      Parameter(
        name = "item",
        description = "The item you want to classify",
        required = true
      )
    ],

    responses = [
      ApiResponse(
        responseCode = "200", content = [
          Content(schema = Schema(implementation = ClassifyResponse::class))
        ]
      )
    ]

  )
  @GetMapping
  fun classify(
    @RequestParam("item") item: String,
  ) = with(restaurantItemClassifier) {
    ClassifyResponse(classifyProtein(item), classifyVegetarian(item), classifyDessert(item))
  }

  data class ClassifyResponse(
    val protein: ProteinItem,
    val vegetarian: VegetarianItem,
    val dessert: DessertItem,
  )

}