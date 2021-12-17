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
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension

apply(plugin = "kotlin-noarg")
apply(plugin = "kotlin")

repositories {
    maven(url = "https://s3-us-west-2.amazonaws.com/dynamodb-local/release")
}

dependencies {
    // Dynamo
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.16.29")
    implementation("br.com.colman.dynamodb:kotlin-dynamodb-extensions:0.2.0")
    testImplementation("com.amazonaws:DynamoDBLocal:1.13.5")

    // YearWeek
    implementation("org.threeten:threeten-extra:1.6.0")

    // Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.1")


    implementation("com.github.haifengl:smile-kotlin:2.6.0")
    implementation("org.apache.commons:commons-csv:1.8")
    implementation("org.apache.lucene:lucene-core:8.9.0")
    implementation("org.apache.lucene:lucene-analyzers:3.6.2")
}

configure<NoArgExtension> {
    annotation("software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean")
    invokeInitializers = true
}
