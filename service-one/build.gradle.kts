plugins {
    id("mixer.java-conventions")
    alias(libs.plugins.spring.boot)
}

springBoot {
    buildInfo()
    mainClass = "it.mixer.demo.SpringBootDemoMixerApplication"
}

dependencies {
    implementation(project(":domain"))
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.bundles.spring.boot.core)
    implementation(libs.springdoc)
    
    testImplementation(libs.bundles.spring.boot.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.spring.boot.webflux.test)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
