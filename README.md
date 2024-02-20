# Jopiter Backend

This project contains all the source code for Jopiter's backend. Every file is released under the [GNU Affero General Public License v3.0 or later](LICENSE), unless stated otherwise.

![Docker](https://img.shields.io/docker/v/jopiterapp/jopiter-backend)

## Production
Current production environment can be found at https://persephone.jopiter.app/api/v1/docs/swagger

## Dockerfile Creation and Tagging

Our Dockerfile is created with a predetermined sequence of steps to set up our environment, and Docker uses it to build an image for the backend.

During the release process, we use a Kotlin-based DSL called github-workflows-kt in our `release.main.kts` script to automate the build, tag, and push of our Docker image to DockerHub. This script handles setting up the Java Development Kit (JDK 17), creating the ShadowJar using `:bootJar` Gradle task, logging into DockerHub, and finally building and pushing the Docker image.

Relevant steps in our release process:

- Setup JDK: `SetupJavaV4(javaVersion = "17", distribution = SetupJavaV4.Distribution.Adopt)`
- Checkout source code.
- Create ShadowJar with `GradleBuildActionV2(arguments = ":bootJar")`
- Login to DockerHub with provided DockerHub username and secret token.
- Build and push Docker image using `BuildPushActionV5`. The image is tagged as **jopiterapp/jopiter-backend:latest** and **jopiterapp/jopiter-backend:$version**

Remember to replace `$version` with the version number for the release. Version number is automatically extracted from the tag of the Git push event.

You can refer to the [release.main.kts](.github/workflows/release.main.kts) script for more details.