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

import org.openqa.selenium.By.className
import org.openqa.selenium.By.id
import org.openqa.selenium.By.linkText
import org.openqa.selenium.By.name
import org.openqa.selenium.By.tagName
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.stereotype.Repository
import java.io.File.createTempFile
import java.time.DayOfWeek
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.MONDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.THURSDAY
import java.time.DayOfWeek.TUESDAY
import java.time.DayOfWeek.WEDNESDAY
import java.time.LocalTime
import java.time.LocalTime.MAX
import java.time.LocalTime.MIN
import java.time.LocalTime.parse

private const val JupiterLoginUrl = "https://uspdigital.usp.br/jupiterweb/webLogin.jsp"

@Repository
class TimetableRepository(
    private val subjectNameRepository: SubjectNameRepository
) {

    init {
        setDriverProperty()
    }

    fun get(user: String, password: String): Set<Subject> = with(firefoxDriver()) {
        try {
            navigateToTimetable(user, password)
            val joinedCourses = waiting { Select(findElement(name("codpgm"))).takeIf { it.options.size > 1 } }
            joinedCourses.options.drop(1).flatMap {
                val timetable = parseCourse(it)
                waiting { invisibilityOfElementLocated(className("blockOverlay")).apply(this) }
                findElement(id("step1-tab")).click()
                timetable.flatMap(TimetableRow::asSubjects)
            }.toSet()
        } finally {
            close()
        }
    }

    private fun parseCourse(course: WebElement) = with(course) {
        click()
        findElement(id("buscar")).click()
        val timetable = findElement(id("tableGradeHoraria"))
        val rows = timetable.findElements(className("ui-widget-content"))
        rows.map { it.findElements(tagName("td")) }.map { TimetableRow(it.map { it.text }) }
    }

    private fun FirefoxDriver.navigateToTimetable(user: String, password: String) = apply {
        get(JupiterLoginUrl)
        findElement(name("codpes")).sendKeys(user)
        findElement(name("senusu")).sendKeys(password)
        findElement(name("Submit")).click()
        waiting { invisibilityOfElementLocated(className("ui-widget-overlay")) }
        findElement(linkText("Grade horária")).click()
    }

    private fun firefoxDriver() = FirefoxDriver(FirefoxOptions().setHeadless(false))

    private fun <T> FirefoxDriver.waiting(block: () -> T?) = WebDriverWait(this, 10).until { block() }!!

    @Suppress("LongParameterList")
    inner class TimetableRow(
        val start: String,
        val end: String,
        val mon: String,
        val tue: String,
        val wed: String,
        val thu: String,
        val fri: String,
        val sat: String,
    ) {
        constructor(trs: List<String>) : this(trs[0], trs[1], trs[2], trs[3], trs[4], trs[5], trs[6], trs[7])

        fun asSubjects(): List<Subject> {
            val subjects = mutableListOf<Subject>()
            val time = if(start.isBlank() || end.isBlank()) MIN..MAX else parse(start)..parse(end)

            if(mon.isNotBlank()) subjects += Subject(MONDAY, mon.code, subjectNameRepository[mon.code], time)
            if(tue.isNotBlank()) subjects += Subject(TUESDAY, tue.code, subjectNameRepository[tue.code], time)
            if(wed.isNotBlank()) subjects += Subject(WEDNESDAY, wed.code, subjectNameRepository[wed.code], time)
            if(thu.isNotBlank()) subjects += Subject(THURSDAY, thu.code, subjectNameRepository[thu.code], time)
            if(fri.isNotBlank()) subjects += Subject(FRIDAY, fri.code, subjectNameRepository[fri.code], time)
            if(sat.isNotBlank()) subjects += Subject(SATURDAY, sat.code, subjectNameRepository[sat.code], time)

            return subjects
        }

        private val String.code get() = substringBefore("-")
    }
}

data class Subject(val dayOfWeek: DayOfWeek, val code: String, val name: String, val time: ClosedRange<LocalTime>)


private fun setDriverProperty() {
    if(System.getProperties().containsKey("webdriver.gecko.driver")) return
    val driver = TimetableRepository::class.java.classLoader.getResourceAsStream("geckodriver")!!
    val path = createTempFile("gecko", "driver").apply {
        writeBytes(driver.readAllBytes())
        setExecutable(true)
    }.absolutePath
    System.setProperty("webdriver.gecko.driver", path)
}
