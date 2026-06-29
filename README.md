# Spring Boot Demo Mixer

This monorepo contains Spring Boot microservices, utilizing Gradle for build automation, centralized dependency management, and automated quality assurance tools.

## Setup Microservices

To execute the microservices locally, please follow these instructions:

1. **Prerequisites**: Ensure Java 17 is installed. Manual installation of Gradle is not required; the provided Gradle wrapper (`./gradlew`) will manage the build environment.
2. **Build the project**: 
   ```bash
   ./gradlew build
   ```
3. **Run a microservice**: Execute the `bootRun` task to initialize a specific service.
   ```bash
   ./gradlew :service-one:bootRun
   ```

## Relevant Gradle Tasks

- `./gradlew build`: Compiles the source code, executes tests, and performs all quality assurance checks across the monorepo.
- `./gradlew test`: Executes unit tests for all microservices.
- `./gradlew :<service-name>:bootRun`: Starts the designated Spring Boot microservice.
- `./gradlew check`: Executes the quality assurance tools (Checkstyle and PMD) without running the test suite.

## Adding a New Microservice

To integrate a new microservice into this monorepo, adhere to the following procedure:

1. **Create the module directory**: Provision a new directory at the project root (e.g., `service-three`).
2. **Register the module**: Append the new module to `settings.gradle.kts`:
   ```kotlin
   include("service-three")
   ```
3. **Configure the build**: Create a `build.gradle.kts` file within the new directory. Apply the shared convention plugin and the Spring Boot framework:
   ```kotlin
   plugins {
       id("mixer.java-conventions")
       alias(libs.plugins.spring.boot)
   }

   springBoot {
       mainClass = "it.mixer.demo.YourApplication"
   }

   dependencies {
       // Define dependencies utilizing the version catalog (e.g., libs.spring.boot.starter.webflux)
   }
   ```
4. **Add source code**: Establish the standard directory layout (`src/main/java/...` and `src/test/java/...`).

## Dependencies

Dependencies are centrally governed using a Gradle Version Catalog, located at `gradle/libs.versions.toml`. 

- When declaring a dependency within a microservice, utilize the `libs.*` syntax (e.g., `implementation(libs.spring.boot.starter.webflux)`).
- This practice guarantees version consistency and mitigates dependency conflicts across discrete microservices.
- Foundational Java configurations (such as Java 17 compliance and Maven Central repository declarations) are centrally administered via the `mixer.java-conventions` plugin situated in the `buildSrc` directory.

## Quality Assurance Tools

Each microservice automatically inherits a standardized suite of Quality Assurance tools via the shared convention plugin. These tools execute automatically during the `./gradlew build` and `./gradlew check` lifecycle phases:

- **Checkstyle**: Enforces consistent coding conventions. The ruleset is maintained at `config/checkstyle/checkstyle.xml`.
- **PMD**: Analyzes source code to identify potential defects, dead code, and suboptimal performance patterns. The configuration is defined in `config/pmd/pmd-ruleset.xml`.
- **JaCoCo**: Generates code coverage metrics. Reports are synthesized automatically post-test execution to verify adequate test density.
