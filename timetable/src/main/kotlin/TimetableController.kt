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
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiContent
import io.javalin.plugin.openapi.annotations.OpenApiRequestBody
import io.javalin.plugin.openapi.annotations.OpenApiResponse

class TimetableController(
    private val timetableRepository: TimetableRepository
) {
    @OpenApi(
        summary = "Fetch a timetable from JupiterWeb",
        description = "Tries to login to user's account and obtain all information related to their timetable",
        tags = ["timetable"],

        requestBody = OpenApiRequestBody([OpenApiContent(TimetableRequest::class)],true),

        responses = [
            OpenApiResponse("200", [OpenApiContent(Subject::class, true)]),
            OpenApiResponse("401"),
            OpenApiResponse("503")
        ]
    )
    fun timetable(context: Context) {
        val (user, password) = context.body<TimetableRequest>()
        try {
            context.json(timetableRepository.get(user, password))
        } catch (_: Throwable) {
            context.status(401)
        }
    }

    data class TimetableRequest(val user: String, val password: String)
}