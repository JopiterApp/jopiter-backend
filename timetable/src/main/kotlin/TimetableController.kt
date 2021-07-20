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
package app.jopiter.timetable

import app.jopiter.timetable.repository.Subject
import app.jopiter.timetable.repository.TimetableRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody as SpringRequestBody

@RestController("\${api.base.path}/timetable")
class TimetableController(
    private val timetableRepository: TimetableRepository,
) {

    @Operation(
        summary = "Fetch a timetable from JupitereWeb",
        description = "Tries to login to user's account and obtain all information related to their timetable",
        tags = ["timetable"],

        requestBody = RequestBody(content = [Content(schema = Schema(implementation = TimetableRequest::class))]),

        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Subject::class)))]
            ),
            ApiResponse(responseCode = "401"),
            ApiResponse(responseCode = "503")
        ]
    )
    @PostMapping()
    fun timetable(@SpringRequestBody request: TimetableRequest): ResponseEntity<Set<Subject>> {
        val (user, password) = request
        return try {
            ResponseEntity.ok(timetableRepository.get(user, password))
        } catch (_: Throwable) {
            ResponseEntity.badRequest().build()
        }
    }

    data class TimetableRequest(val user: String, val password: String)
}
