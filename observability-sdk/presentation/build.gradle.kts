plugins {
    id("observability.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.presentation"
}

dependencies {
    implementation(project(":observability-sdk:data"))
    implementation(project(":observability-sdk:domain"))
}