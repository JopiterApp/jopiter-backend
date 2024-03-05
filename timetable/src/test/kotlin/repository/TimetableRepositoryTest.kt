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

import io.kotest.core.annotation.EnabledCondition
import io.kotest.core.annotation.EnabledIf
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.lang.System.getenv
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalTime.of
import kotlin.reflect.KClass

class UspVariablesCondition : EnabledCondition {
  override fun enabled(specKlass: KClass<out Spec>) = "USP_USER_1" in getenv() && "USP_USER_2" in getenv()
}

@EnabledIf(UspVariablesCondition::class)
class TimetableRepositoryTest : ShouldSpec({

  val target = TimetableRepository(SubjectNameRepository())

  should("Return the right subjects (Test 1)") {
    val answer = target.get(getenv("USP_USER_1"), getenv("USP_PASSWORD_1"))

    answer shouldBe setOf(
      Subject(MONDAY, "ACH2017", "Projeto Supervisionado ou de Graduação I", of(12, 0)..of(13, 0)),
      Subject(WEDNESDAY, "ACH2076", "Segurança da Informação", of(8, 0)..of(12, 0)),
      Subject(THURSDAY, "ACH2167", "Computação Sônica", of(19, 0)..of(22, 45))
    )
  }

  should("Return the right subjects (Test 2)") {
    val answer = target.get(getenv("USP_USER_2"), getenv("USP_PASSWORD_2"))

    answer shouldBe setOf(
      Subject(MONDAY, "PMT3100", "Fundamentos de Ciência e Engenharia dos Materiais", of(15, 0)..of(16, 40)),
      Subject(WEDNESDAY, "MAT2454", "Cálculo Diferencial e Integral II", of(11, 10)..of(12, 50)),
      Subject(FRIDAY, "MAT2454", "Cálculo Diferencial e Integral II", of(11, 10)..of(12, 50)),
      Subject(SATURDAY, "PCS3617", "Estágio Cooperativo I", of(14, 0)..of(15, 40))
    )
  }
})
