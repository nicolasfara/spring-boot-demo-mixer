plugins {
    id("mixer.java-conventions")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":domain"))
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.bundles.spring.boot.core)
    
    testImplementation(libs.bundles.spring.boot.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}



