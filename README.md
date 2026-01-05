# Observability SDK

Este proyecto es un SDK de observabilidad para Android, diseñado con una arquitectura modular y limpia. Permite el seguimiento de eventos y pantallas dentro de una aplicación, persistencia local de datos, sincronización con un servidor remoto y la provisión de datos para la visualización de métricas.

## ✨ Características

- **Arquitectura Modular**: Separación clara de responsabilidades en capas de `data`, `domain` y `presentation`.
- **Inyección de Dependencias**: Configurado con Koin para un manejo desacoplado y eficiente de las dependencias, incluyendo optimizaciones de rendimiento.
- **Persistencia Local**: Utiliza Room para almacenar datos de pantallas e incidentes, con migraciones para gestionar cambios de esquema de forma segura.
- **API Pública Encapsulada**: Expone una única interfaz (`ContractObservabilityApi`) para interactuar con el SDK, ocultando todos los detalles de implementación (`ViewModel`, `UseCases`, etc.) y siguiendo el patrón de diseño de Contrato.
- **Provisión de Datos para Visualización**: El SDK procesa y expone un `StateFlow` (`MainState`) que contiene todas las métricas necesarias (como contadores de incidentes por severidad) para que una aplicación cliente pueda construir fácilmente visualizaciones ricas.
- **Filtrado Dinámico**: La API permite enviar acciones para filtrar los datos por pantalla, severidad del incidente y múltiples rangos de tiempo (`TimeFilter`).
- **Pruebas Unitarias**: Cobertura de pruebas para la capa de `domain` (`UseCases`) usando `MockK` para asegurar la fiabilidad de la lógica de negocio.
- **Automatización de Builds**: Tareas de Gradle personalizadas para automatizar la limpieza, prueba y compilación de la librería.

## 📚 Estructura de Módulos

- **/observability-sdk**: El corazón de la librería, dividido en:
  - **:presentation**: Expone la API pública del SDK (`ContractObservabilityApi`) y contiene la lógica del ViewModel.
  - **:domain**: Contiene la lógica de negocio pura, las interfaces de los repositorios y los `UseCases`.
  - **:data**: Implementa los repositorios, manejando las fuentes de datos (Room y Ktor).
- **/build-logic**: Centraliza la lógica de compilación de Gradle.

## 🚀 Build y Automatización con Gradle

El proyecto está configurado con tareas personalizadas de Gradle para optimizar el flujo de desarrollo y asegurar la calidad del código.

### Tarea `buildDevAars`

La tarea principal de integración continua es `buildDevAars`. Esta se encarga de ejecutar las pruebas unitarias y, si tienen éxito, ensamblar los artefactos `.aar` para cada módulo de la librería.

#### Artefactos Generados

Al ejecutar esta tarea, se generarán tres artefactos tipo .aar, uno por cada módulo del SDK, en las siguientes rutas:

- **Data**: `:observability-sdk/data/build/outputs/aar/data-dev.aar`
- **Domain**: `:observability-sdk/domain/build/outputs/aar/domain-dev.aar`
- **Presentation**: `:observability-sdk:presentation/build/outputs/aar/presentation-dev.aar`

### Cómo Ejecutar las Tareas

#### 1. Desde la Línea de Comandos (Recomendado)

Para limpiar el proyecto, ejecutar las pruebas y construir los artefactos en un solo paso, abre una terminal en la raíz del proyecto y ejecuta:

```bash
./gradlew cleanBuilds buildDevAars
```

#### 2. Desde Android Studio (Manualmente)

También puedes ejecutar estas tareas directamente desde el IDE:

1.  Abre la ventana de herramientas de **Gradle** (usualmente en el panel derecho).
2.  Navega a **`ObservabilitySDK` > `Tasks` > `Build`**.
3.  Aquí encontrarás las tareas `buildDevAars` y `cleanBuilds`. Haz doble clic en cualquiera de ellas para ejecutarla.

## 🛠️ Cómo Usar el SDK

La integración del SDK en una aplicación cliente se realiza a través de Koin y la API pública `ContractObservabilityApi`.

### 1. Inyección de Módulos Koin

Cada módulo del SDK (`data`, `domain`, `presentation`) expone su propio módulo de Koin. La aplicación cliente es responsable de iniciar Koin y cargar estos módulos. Esto se hace en una clase `Application` personalizada.

**Paso 1: Crear la clase `Application`**

```kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
```

**Paso 2: Registrar la clase en el `AndroidManifest.xml`**

Es crucial registrar esta clase en el manifiesto de la aplicación cliente. Añade el atributo `android:name` a la etiqueta `<application>`. Además, si tu servidor de pruebas no usa HTTPS, debes permitir el tráfico de texto plano:

```xml
<application
    android:name=".MainApplication"
    android:usesCleartextTraffic="true"
    ...
    >
    <!-- ... el resto de tu manifiesto ... -->
</application>
```

**Nota Importante**: El atributo `android:usesCleartextTraffic="true"` es necesario durante el desarrollo ya que el servidor backend local no usa HTTPS. Permite que la aplicación realice peticiones HTTP.

### 2. Interacción con la API del SDK

Una vez que Koin está configurado, la UI de la aplicación cliente puede solicitar una instancia de `ContractObservabilityApi` y comenzar a interactuar con ella.

- **Inyectar la API**:
  ```kotlin
  val sdk: ContractObservabilityApi = koinViewModel()
  ```

- **Observar el Estado**:
  ```kotlin
  val state by sdk.state.collectAsStateWithLifecycle()
  ```

- **Enviar Eventos**:
  ```kotlin
  sdk.onEvent(MainActions.InsertScreen("LoginScreen"))
  ```

## 💻 Pila Tecnológica

- **Lenguaje**: Kotlin
- **Inyección de Dependencias**: Koin
- **Base de Datos**: Room
- **Red**: Ktor
- **Pruebas**: JUnit 4, MockK
- **Automatización**: Gradle, Script de Lotes de Windows (.bat)
