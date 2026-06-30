plugins {
    id("mixer.java-conventions")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.springdoc)
    implementation("io.projectreactor:reactor-core")
}
