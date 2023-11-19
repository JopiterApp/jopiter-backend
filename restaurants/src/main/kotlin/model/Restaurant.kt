/*
 * Jopiter APP
 * Copyright (C) 2021 Leonardo Colman Lopes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.jopiter.restaurants.model

import app.jopiter.restaurants.model.Restaurant.Central
import app.jopiter.restaurants.model.Restaurant.CentralRibeirao
import app.jopiter.restaurants.model.Restaurant.Crhea
import app.jopiter.restaurants.model.Restaurant.Each
import app.jopiter.restaurants.model.Restaurant.Eel1
import app.jopiter.restaurants.model.Restaurant.Eel2
import app.jopiter.restaurants.model.Restaurant.EscolaDeEnfermagem
import app.jopiter.restaurants.model.Restaurant.FacDireito
import app.jopiter.restaurants.model.Restaurant.Fisica
import app.jopiter.restaurants.model.Restaurant.Piracicaba
import app.jopiter.restaurants.model.Restaurant.Pirassununga
import app.jopiter.restaurants.model.Restaurant.PuSPC
import app.jopiter.restaurants.model.Restaurant.Quimicas
import app.jopiter.restaurants.model.Restaurant.RestauranteArea1
import app.jopiter.restaurants.model.Restaurant.RestauranteArea2
import app.jopiter.restaurants.model.Restaurant.SaudePublica
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Required for OpenAPI to work
 *
 * https://github.com/swagger-api/swagger-core/issues/3691
 */
@Schema(name = "Campus")
private interface CampusSer {
    @get:Schema(example = "Cidade Universitária") val campusName: String
    val restaurants: List<RestaurantSer>
}

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Schema(implementation = CampusSer::class)
enum class Campus(val campusName: String, val restaurants: List<Restaurant>) {
    CidadeUniversitaria("Cidade Universitária", listOf(Central, PuSPC, Fisica, Quimicas)),
    QuadrilateroSaude("Quadrilátero Saúde", listOf(EscolaDeEnfermagem, SaudePublica)),
    LargoSaoFrancisco("Largo São Francisco", listOf(FacDireito)),
    UspLeste("USP Leste", listOf(Each)),
    Bauru("Campus de Bauru", listOf(Restaurant.Bauru)),
    LuizDeQueiroz("Campus \"Luiz de Queiroz\"", listOf(Piracicaba)),
    FernandoCosta("Campus \"Fernando Costa\"", listOf(Pirassununga)),
    SaoCarlos("Campus de São Carlos", listOf(Crhea, RestauranteArea1, RestauranteArea2)),
    RibeiraoPreto("Campus de Ribeirão Preto", listOf(CentralRibeirao)),
    Lorena("Campus de Lorena", listOf(Eel1, Eel2))
}

/**
 * Required for OpenAPI to work
 *
 * https://github.com/swagger-api/swagger-core/issues/3691
 */
@Schema(name = "Restaurant")
private interface RestaurantSer {
    @get:Schema(example = "9") val id: Int
    @get:Schema(example = "Químicas") val restaurantName: String
}

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Restaurant(val id: Int, val restaurantName: String) {

    // Cidade Universitária
    Central(6, "Central - Campus Butantã"),
    PuSPC(7, "PUSP-C - Campus Butantã"),
    Fisica(8, "Física - Campus Butantã"),
    Quimicas(9, "Químicas - Campus Butantã"),

    // Quatrilátero Saúde
    EscolaDeEnfermagem(12, "Escola de Enfermagem"),
    SaudePublica(11, "Fac. Saúde Pública"),

    // Largo São Francisco
    FacDireito(14, "Fac. Direito"),

    // USP Leste
    Each(13, "EACH"),

    // Baurú
    Bauru(20, "Bauru"),

    // Luiz de Queiroz
    Piracicaba(1, "Piracicaba"),

    // Fernando Costa
    Pirassununga(5, "Pirassununga"),

    // São Carlos
    Crhea(4, "Restaurante CRHEA"),
    RestauranteArea1(2, "Restaurante área 1"),
    RestauranteArea2(3, "Restaurante área 2"),

    // Ribeirão
    CentralRibeirao(19, "Restaurante Central -Campus RP"),

    // Lorena
    Eel1(17, "EEL - Área I"),
    Eel2(23, "EEL - Área II"),;

    companion object {
        fun getById(id: Int) = values().first { it.id == id }
    }
}
