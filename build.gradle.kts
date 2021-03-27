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

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


group = "app.jopiter"

plugins {
    application
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.31" apply false
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

@Suppress("DEPRECATION")
application.mainClassName = "app.jopiter.JavalinAppKt"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven(url = "https://jitpack.io")
    }

    dependencies {
        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")

        // Javalin
        implementation("io.javalin:javalin:3.13.4")
        implementation("io.javalin:javalin-openapi:3.13.4") {
            exclude("io.swagger.parser.v3", "swagger-parser")
        }

        // Koin
        implementation("org.koin:koin-core:2.2.0")
        testImplementation("org.koin:koin-test:2.2.0")

        // SLF4J
        implementation("org.slf4j:slf4j-simple:1.7.30")

        // Log4J
        implementation("org.apache.logging.log4j:log4j-core:2.14.0")

        // Fuel
        implementation("com.github.kittinunf.fuel:fuel:2.3.1")
        implementation("com.github.kittinunf.fuel:fuel-jackson:2.3.1")
        implementation("com.github.kittinunf.fuel:fuel-coroutines:2.3.1")

        // Kotest
        testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
        testImplementation("io.kotest:kotest-assertions-json:4.4.3")
        testImplementation("io.kotest:kotest-extensions-koin:4.4.3")
        testImplementation("io.kotest:kotest-property:4.4.3")

        // Mockk
        testImplementation("io.mockk:mockk:1.11.0")

        // Mock Server
        testImplementation("org.mock-server:mockserver-netty:5.11.2")

        // Root project
        testImplementation(rootProject)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
            exceptionFormat = FULL
            showCauses = true
            showStandardStreams = true
            events(FAILED, PASSED)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation(project(":privacy"))
    implementation(project(":restaurants"))
}
