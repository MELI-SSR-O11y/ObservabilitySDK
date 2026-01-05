// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  base
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotzilla) apply false
}

tasks.register("cleanBuilds") {
  group = "Build"
  description = "Limpia los modulos."

  dependsOn(
    ":observability-sdk:presentation:clean",
    ":observability-sdk:domain:clean",
    ":observability-sdk:data:clean",
  )

  doLast {
    println(">>> Tarea 'cleanBuilds' completada: ")
  }
}
tasks.register("buildDevAars") {
  group = "Build"
  description = "Ensambla los AARs de la variante 'dev' para los módulos de la librería."

  dependsOn(
    ":observability-sdk:domain:testDevUnitTest",
    ":observability-sdk:presentation:assembleDev",
    ":observability-sdk:domain:assembleDev",
    ":observability-sdk:data:assembleDev",
  )

  doLast {
    println(">>> Tarea 'buildDevAars' completada: Ensamblado de los AARs 'dev' han finalizado.")
  }
}
