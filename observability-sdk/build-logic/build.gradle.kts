import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("androidLibraryConvention") {
            id = "observability.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("android.gradlePlugin").get())
    implementation(libs.findLibrary("kotlin.gradlePlugin").get())
}