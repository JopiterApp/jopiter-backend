#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.8.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile


workflow(
    name = "Test",
    on = listOf(Push()),
    sourceFile = __FILE__.toPath()
) {
    job(id = "Test", runsOn = UbuntuLatest) {
        uses(name = "Setup JDK", action = SetupJavaV4(javaVersion = "17", distribution = SetupJavaV4.Distribution.Adopt))
        uses(name = "Checkout", action = CheckoutV4())
        uses(name = "Run Test", action = GradleBuildActionV2(arguments = "test"))

    }
}.writeToFile()