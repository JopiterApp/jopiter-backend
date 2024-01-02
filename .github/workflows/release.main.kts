#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.8.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.azure.DockerLoginV1
import io.github.typesafegithub.workflows.actions.docker.BuildPushActionV5
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Release
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

val version = expr { Contexts.github.ref }
val DOCKER_HUB_USERNAME by Contexts.secrets
val DOCKER_HUB_TOKEN by Contexts.secrets
val dockerNamespace = "jopiterapp/jopiter-backend"

workflow(
    name = "Release",
    on = listOf(Release()),
    sourceFile = __FILE__.toPath()
) {
    job(id = "Release", runsOn = UbuntuLatest) {
        uses(
            name = "Setup JDK",
            action = SetupJavaV4(javaVersion = "17", distribution = SetupJavaV4.Distribution.Adopt)
        )
        uses(name = "Checkout", action = CheckoutV4())
        uses(name = "Create ShadowJar", action = GradleBuildActionV2(arguments = ":bootJar"))
        uses(
            name = "Login to DockerHub",
            action = DockerLoginV1(username = expr { DOCKER_HUB_USERNAME }, password = expr { DOCKER_HUB_TOKEN })
        )
        uses(
            name = "Build and Push",
            action = BuildPushActionV5(
                context = ".",
                push = true,
                tags = listOf("$dockerNamespace/latest", "$dockerNamespace/$version")
            )
        )
    }
}.writeToFile()