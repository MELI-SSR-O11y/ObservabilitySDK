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
- **/build-logic**: Centraliza la lógica de compilación de Gradle, incluyendo la definición de las variantes de compilación.

## 🚀 Build y Automatización con Gradle

El proyecto está configurado con tareas personalizadas de Gradle para optimizar el flujo de desarrollo y asegurar la calidad del código.

### Build Variants y Configuración de Entorno

La configuración de compilación está centralizada en `build-logic/src/main/kotlin/AndroidLibraryConventionPlugin.kt`. Se han definido tres variantes (build types) para el SDK:

- **`dev`**: Para desarrollo local.
- **`qa`**: Para el entorno de Quality Assurance.
- **`release`**: La versión de producción.

#### Configuración para el Entorno de Desarrollo (`dev`)

Para que la aplicación cliente se pueda comunicar con el servidor backend durante el desarrollo, ambos dispositivos (donde corre el backend y donde corre la app Android) deben estar conectados a la **misma red Wi-Fi**.

La `BASE_URL` para la variante `dev` apunta a una dirección IP local que debe ser configurada manualmente.

**¿Cómo encontrar y configurar la IP local?**

1.  **Obtén la dirección IP de la máquina donde corre el backend**:
    -   En **Windows**: Abre `cmd` y ejecuta el comando `ipconfig`. Busca la dirección `IPv4` de tu adaptador de Wi-Fi.
    -   En **macOS**: Abre la `Terminal` y ejecuta el comando `ifconfig | grep "inet "`. Busca la dirección IP que usualmente empieza con `192.168.x.x`.

2.  **Actualiza el archivo de configuración**:
    -   Navega a `observability-sdk/build-logic/src/main/kotlin/AndroidLibraryConventionPlugin.kt`.
    -   Busca la variante `dev` y reemplaza la IP `192.168.1.3` por la dirección IP que obtuviste en el paso anterior.

    ```kotlin
    create("dev") {
        initWith(getByName("debug"))
        buildConfigField("String", "BASE_URL", "\"http://TU_IP_LOCAL:8080/\"")
        // ...
    }
    ```

#### Variables de Entorno

Cada variante configura los siguientes parámetros en el `BuildConfig` del módulo de `data`:

- `BASE_URL`: La URL del servidor backend.
- `LOGS_ENABLED`: Un booleano para activar o desactivar los logs.
- `X_API_KEY`: La clave de API para autenticarse. Esta clave se puede sobreescribir estableciendo una variable de entorno `X_API_KEY` en la máquina de compilación.

### Tarea `buildDevAars`

La tarea principal de integración continua es `buildDevAars`. Esta se encarga de ejecutar las pruebas unitarias y, si tienen éxito, ensamblar los artefactos `.aar` para cada módulo de la librería.

#### Artefactos Generados

Al ejecutar esta tarea, se generarán tres artefactos tipo .aar, uno por cada módulo del SDK, en las siguientes rutas:

- **Data**: `:observability-sdk/data/build/outputs/aar/data-dev.aar`
- **Domain**: `:observability-sdk/domain/build/outputs/aar/domain-dev.aar`
- **Presentation**: `:observability-sdk:presentation/build/outputs/aar/presentation-dev.aar`

> **Nota**: Para mayor comodidad, se incluyen versiones pre-compiladas de estos artefactos en la carpeta [artifacts](./artifacts) del repositorio. Estos pueden ser utilizados directamente en un proyecto sin necesidad de compilar la librería.

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

### 1. Inyección de Módulos Koin

Cada módulo del SDK (`data`, `domain`, `presentation`) expone su propio módulo de Koin. La aplicación cliente es responsable de iniciar Koin y cargar los módulos del SDK (`dataModule`, `domainModule`, `presentationModule`).

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
- **Automatización y Compilación**: Gradle

## 🚀 Próximos Pasos y Mejoras Futuras*
*
Si se dispusiera de más tiempo, las siguientes mejoras podrían llevar el SDK al siguiente nivel:

- **Sistema de Reconocimiento de Dispositivo**: Implementar un sistema que capture el tipo de dispositivo, versión de Android y sus capacidades. Esto permitiría una clasificación más precisa de las fallas para identificar si están correlacionadas con versiones específicas del SO o limitaciones de hardware.

- **Reportes Automáticos por Crash**: Automatizar la generación de reportes de incidentes. Por ejemplo, si la aplicación crashea al consumir un servicio, el SDK debería interceptar el fallo y generar un reporte de incidente automáticamente antes de que la app se cierre.

- **Integración con Firebase Crashlytics**: Conectar el SDK con un servicio de monitoreo líder en la industria como Firebase Crashlytics. Esto proporcionaría un dashboard centralizado para el registro y análisis de fallos en producción.

- **Monitoreo de Fugas de Memoria**: Integrar una librería como **LeakCanary** para monitorear en tiempo real las fugas de memoria durante el desarrollo, asegurando una mayor estabilidad de la aplicación.

- **Análisis de Calidad y Seguridad del Código**: Implementar una herramienta de análisis estático en tiempo real como **SonarQube** o **Datadog**. Esto ayudaría a mantener un alto estándar de calidad de código, identificar vulnerabilidades de seguridad y optimizar el rendimiento de forma proactiva.

- **Optimización de Consultas a la Base de Datos**: A nivel de proyecto, delegar la lógica de filtrado de datos directamente a la base de datos (mediante queries en el DAO) en lugar de hacerlo en la capa de repositorio con lambdas. Aunque la implementación actual demuestra el manejo de colecciones y uso de funciones lambda en Kotlin, mover esta lógica a Room mejoraría significativamente el rendimiento de la aplicación al reducir la cantidad de datos procesados en memoria.

- **Mejora de la Experiencia de Desarrollo (DX)**: Crear un módulo `/app` que sirva como un entorno de pruebas y ejecución de los modulos data, domain, presentation en tiempo real. Esto elimina la necesidad de compilar artefactos (`.aar`), copiarlos y pegarlos en un proyecto secundario, reduciendo drásticamente los tiempos de sincronización y compilación para el desarrollador que mantiene el SDK.

- **Sistema de Versionamiento y Despliegue con JFrog**: Implementar un sistema de versionamiento y despliegue automatizado en un repositorio de artefactos como **JFrog Artifactory**. Esto eliminaría el proceso manual de compilar `.aar`s locales. La integración en la aplicación de pruebas (o en cualquier app cliente) se reduciría a simplemente cambiar el número de versión de la dependencia en Gradle.


## 📝 Post Mortem: Reflexiones del Desarrollo

Uno de los principales desafíos del proyecto fue diseñar una arquitectura escalable partiendo de unos requerimientos funcionales que, a primera vista, parecían sencillos. Aunque la prueba técnica no exigía explícitamente un backend, un consumo de servicios o una estructura modular, se tomó la decisión estratégica de dedicar tiempo a la planeación y análisis para ir más allá del alcance inicial.

El objetivo fue construir una solución robusta que no solo cumpliera con los requisitos, sino que también demostrara la capacidad de desarrollar aplicaciones modulares y escalables, integrando conocimientos tanto de desarrollo móvil como de backend para lograr un ecosistema completo y dinámico con un enfoque de desarrollo end-to-end.

El acierto más significativo fue la adopción de los principios de Arquitectura Limpia, separando el proyecto en las capas `data`, `domain` y `presentation`. Esta inversión inicial en una base desacoplada demostró su valor a lo largo del desarrollo, permitiendo implementar cambios y nuevas funcionalidades de manera ágil y segura, sin generar efectos secundarios en otras partes del sistema. Esto reafirma que una arquitectura bien planificada es el pilar fundamental para la mantenibilidad y evolución de cualquier software.