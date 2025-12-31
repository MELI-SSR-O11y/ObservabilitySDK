plugins {
  id("observability.android.library")
  alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.data"
}

dependencies {
  implementation(project(":observability-sdk:domain"))
}