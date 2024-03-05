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

plugins {
  kotlin("plugin.spring")
  kotlin("plugin.jpa")
  id("org.flywaydb.flyway") version "9.22.3"
}

dependencies {
  // Projects
  implementation(project(":restaurants:classifier"))

  // SpringData
  implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

  // FlywayDB
  implementation("org.flywaydb:flyway-core:9.22.3")

  // KTORM
  implementation("org.ktorm:ktorm-core:3.5.0")
  implementation("org.ktorm:ktorm-support-postgresql:3.5.0")

  // Postgres
  implementation("org.postgresql:postgresql")

  // YearWeek
  implementation("org.threeten:threeten-extra:1.7.0")

  // Caffeine
  implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
}

flyway {
  url = System.getenv("JDBC_URL") ?: "jdbc:postgresql://localhost:5432/jopiter"
  user = System.getenv("DATABASE_USER") ?: "jopiter"
  password = System.getenv("DATABASE_PASS") ?: "password"
}
