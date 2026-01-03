plugins {
  id("observability.android.library")
  alias(libs.plugins.kotlin.android)
  id("kotlin-kapt")
}

android {
    namespace = "com.example.data"
}

dependencies {
  implementation(project(":observability-sdk:domain"))
  implementation(libs.bundles.ktor.common)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.androidx.room.runtime)
  implementation(libs.sqlite.bundled)
  implementation(libs.bundles.koin.common)
  kapt(libs.androidx.room.compiler)
}