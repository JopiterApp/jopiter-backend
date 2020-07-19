/*
 * Jopiter App
 * Copyright (C) 2020 Leonardo Colman Lopes
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

import io.kotless.plugin.gradle.dsl.kotless

group = "app.jopiter"
version = "1.0.0"

plugins {
    kotlin("jvm") version "1.3.72"
    id("io.kotless") version "0.1.5"
}

kotless {
    config {
        bucket = "app.jopiter.backend"

        terraform {
            profile = "jopiter"
            region = "us-east-1"
        }
    }

    webapp {
        lambda {
            timeoutSec = 10
            memoryMb = 512
        }
    }
}

allprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
    }
    
    dependencies {
        // Kotlin
        implementation(kotlin("stdlib-jdk8"))

        // Kotless
        implementation("io.kotless:ktor-lang:0.1.5")

        // Kotest
        testImplementation("io.kotest:kotest-runner-junit5:4.1.2")
        testImplementation("io.kotest:kotest-runner-console-jvm:4.1.2")

        // Mockk
        testImplementation("io.mockk:mockk:1.10.0")

        // KTor
        testImplementation("io.ktor:ktor-server-test-host:1.3.2")
    }
}
