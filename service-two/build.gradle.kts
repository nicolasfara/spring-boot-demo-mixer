plugins {
    id("mixer.java-conventions")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":domain"))
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.bundles.spring.boot.core)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(libs.springdoc)
    
    testImplementation(libs.bundles.spring.boot.test)
    testImplementation(libs.reactor.test)
    testImplementation(libs.mockwebserver)
    testRuntimeOnly(libs.junit.platform.launcher)
}



