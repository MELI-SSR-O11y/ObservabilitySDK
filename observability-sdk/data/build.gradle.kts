plugins {
  id("observability.android.library")
  alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.data"
}

dependencies {
  implementation(project(":observability-sdk:domain"))
  implementation(libs.bundles.ktor.common)
  implementation(libs.ktor.client.okhttp)
}