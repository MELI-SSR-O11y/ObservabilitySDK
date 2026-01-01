import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.android")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 28

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    val apiKey = System.getenv("X_API_KEY")

                    release {
                        isMinifyEnabled = false
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                        buildConfigField("String", "BASE_URL", "\"https://meli.com/\"")
                        buildConfigField("boolean", "LOGS_ENABLED", "false")
                        buildConfigField("String", "X_API_KEY", "\"${apiKey ?: "7a66d694-61eb-4fb4-ad22-916f34c6bcab"}\"")
                    }
                    create("dev") {
                        initWith(getByName("debug"))
                        buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
                        buildConfigField("boolean", "LOGS_ENABLED", "true")
                        buildConfigField("String", "X_API_KEY", "\"${apiKey ?: "ec1cbf5b-a671-42ce-a3c5-0bdba6d44f21"}\"")
                    }
                    create("qa") {
                        initWith(getByName("debug"))
                        buildConfigField("String", "BASE_URL", "\"https://qa.meli.com/\"")
                        buildConfigField("boolean", "LOGS_ENABLED", "true")
                        buildConfigField("String", "X_API_KEY", "\"${apiKey ?: "3231d333-2ea9-4b8c-b0f4-1395f6a31fc2"}\"")
                    }
                }
                buildFeatures {
                    buildConfig = true
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                publishing {
                    singleVariant("release")
                    singleVariant("dev")
                    singleVariant("qa")
                }
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("androidx.appcompat").get())
                add("implementation", libs.findLibrary("material").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
                add("implementation", libs.findLibrary("touchlab.kermit").get())
            }
        }
    }
}