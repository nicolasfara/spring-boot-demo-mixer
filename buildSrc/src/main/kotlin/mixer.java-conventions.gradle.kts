plugins {
    java
    checkstyle
    pmd
    jacoco
}

group = "it.mixer"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(26))
    }
}

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

checkstyle {
    toolVersion = "10.12.5"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
}

tasks.withType<Checkstyle> {
    reports {
        sarif.required.set(true)
    }
}

pmd {
    toolVersion = "6.55.0"
    isConsoleOutput = true
    ruleSets = listOf() // clear default rulesets
    ruleSetFiles = rootProject.files("config/pmd/pmd-ruleset.xml")
    isIgnoreFailures = false
}

tasks.withType<Pmd> {
    reports {
        sarif.required.set(true)
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}


