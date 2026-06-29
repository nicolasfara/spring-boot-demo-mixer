plugins {
    id("mixer.java-conventions")
    alias(libs.plugins.spring.boot)
}

springBoot {
    buildInfo()
    mainClass = "it.mixer.demo.SpringBootDemoMixerApplication"
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.springdoc)
    
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.reactor.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
