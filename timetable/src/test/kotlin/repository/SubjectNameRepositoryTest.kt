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

package app.jopiter.timetable.repository

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SubjectNameRepositoryTest : ShouldSpec({

    val target = SubjectNameRepository()

    should("Fetch the subjects name correctly") {
        target["ACH2017"] shouldBe "Projeto Supervisionado ou de Graduação I"
        target["ACH2076"] shouldBe "Segurança da Informação"
        target["PMT3100"] shouldBe "Fundamentos de Ciência e Engenharia dos Materiais"
        target["MAT2454"] shouldBe "Cálculo Diferencial e Integral II"
        target["PCS3617"] shouldBe "Estágio Cooperativo I"
    }

    should("Throw exception when fetching an unknown subject") {
        shouldThrowAny { target["ablueblua"] }
    }

})
