plugins {
    id("observability.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.domain"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.android.mock)
    testImplementation(libs.koitlinx.coroutine.test)
    testImplementation(kotlin("test"))
}