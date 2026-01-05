# Observability SDK

Este proyecto es un SDK de observabilidad para Android, diseñado con una arquitectura modular y limpia. Permite el seguimiento de eventos y pantallas dentro de una aplicación, persistencia local de datos, sincronización con un servidor remoto y la provisión de datos para la visualización de métricas.

## ✨ Características

- **Arquitectura Modular**: Separación clara de responsabilidades en capas de `data`, `domain` y `presentation`.
- **Inyección de Dependencias**: Configurado con Koin para un manejo desacoplado y eficiente de las dependencias, incluyendo optimizaciones de rendimiento.
- **Persistencia Local**: Utiliza Room para almacenar datos de pantallas e incidentes, con migraciones para gestionar cambios de esquema de forma segura.
- **API Pública Encapsulada**: Expone una única interfaz (`ContractObservabilityApi`) para interactuar con el SDK, ocultando todos los detalles de implementación (`ViewModel`, `UseCases`, etc.) y siguiendo el patrón de diseño de Contrato.
- **Provisión de Datos para Visualización**: El SDK procesa y expone un `StateFlow` (`MainState`) que contiene todas las métricas necesarias (como contadores de incidentes por severidad) para que una aplicación cliente pueda construir fácilmente visualizaciones ricas, como gráficos de torta o de series de tiempo. El módulo `/app` sirve como una implementación de referencia.
- **Filtrado Dinámico**: La API permite enviar acciones para filtrar los datos por pantalla, severidad del incidente y múltiples rangos de tiempo (`TimeFilter`).
- **Pruebas Unitarias**: Cobertura de pruebas para la capa de `domain` (`UseCases`) usando `MockK` para asegurar la fiabilidad de la lógica de negocio.
- **Automatización de Builds**: Tareas de Gradle personalizadas para automatizar la limpieza, prueba y compilación de la librería.

## 📚 Estructura de Módulos

- **/app**: Una aplicación de ejemplo que consume el SDK y demuestra cómo construir una UI para visualizar los datos provistos.
- **/observability-sdk**: El corazón de la librería, dividido en:
  - **:presentation**: Expone la API pública del SDK (`ContractObservabilityApi`) y contiene la lógica del ViewModel.
  - **:domain**: Contiene la lógica de negocio pura, las interfaces de los repositorios y los `UseCases`.
  - **:data**: Implementa los repositorios, manejando las fuentes de datos (Room y Ktor).
- **/build-logic**: Centraliza la lógica de compilación de Gradle.

## 🚀 Build y Automatización con Gradle

El proyecto está configurado con tareas personalizadas de Gradle para optimizar el flujo de desarrollo y asegurar la calidad del código.

### Tareas Personalizadas

Se han creado las siguientes tareas en el archivo `build.gradle.kts` raíz:

1.  **`cleanBuilds`**: Una tarea de utilidad que limpia los directorios de compilación de los tres módulos del SDK (`presentation`, `domain`, `data`).
2.  **`buildDevAars`**: La tarea principal de integración continua. Ejecuta las pruebas unitarias de los tres módulos y, solo si todas pasan, procede a ensamblar los artefactos `.aar` de la variante `dev`.

El orden de ejecución está garantizado con la regla `mustRunAfter`, lo que significa que el ensamblaje no comenzará si una prueba falla.

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

## 🛠️ Cómo Usar el SDK (Ejemplo en la App)

La interacción con el SDK desde una aplicación cliente se realiza a través del contrato `ContractObservabilityApi`.

1. **Inyectar la API del SDK**:
   Usa Koin para obtener una instancia del contrato en tu Composable.

   ```kotlin
   val sdk: ContractObservabilityApi = koinViewModel()
   ```

2. **Observar el Estado**:
   Recolecta el `state` para que tu UI reaccione a los cambios y el onEvent para enviar acciones.

   ```kotlin
   val state by sdk.state.collectAsStateWithLifecycle()
   val onEvent = api::onEvent
   ```

3. **Enviar Eventos (Acciones)**:
   Usa la función `onEvent` para enviar acciones al SDK.

   ```kotlin
   onEvent(MainActions.RollbackFromRemote)
   onEvent(MainActions.FilterByTime(TimeFilter.Last30Minutes))
   ```

## 💻 Pila Tecnológica

- **Lenguaje**: Kotlin
- **Inyección de Dependencias**: Koin
- **Base de Datos**: Room
- **Red**: Ktor
- **Pruebas**: JUnit 4, MockK
- **Automatización**: Gradle, Script de Lotes de Windows (.bat)
