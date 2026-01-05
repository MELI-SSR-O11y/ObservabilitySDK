# Observability SDK

Este proyecto es un SDK de observabilidad para Android, diseñado con una arquitectura modular y limpia. Permite el seguimiento de eventos y pantallas dentro de una aplicación, persistencia local de datos, sincronización con un servidor remoto y visualización de métricas a través de una aplicación de ejemplo.

## ✨ Características

- **Arquitectura Modular**: Separación clara de responsabilidades en capas de `data`, `domain` y `presentation`.
- **Inyección de Dependencias**: Configurado con Koin para un manejo desacoplado y eficiente de las dependencias.
- **Persistencia Local**: Utiliza Room para almacenar datos de pantallas e incidentes de forma local.
- **Sincronización Remota**: Capacidad para sincronizar los datos locales con un servidor backend a través de Ktor.
- **API Pública Encapsulada**: Expone una única interfaz (`ContractObservabilityApi`) para interactuar con el SDK, ocultando los detalles de implementación.
- **Visualización de Datos**: La aplicación de ejemplo (`Observability App`) muestra métricas en tiempo real, incluyendo gráficos de torta y de series de tiempo.
- **Filtrado Dinámico**: Permite filtrar los datos por pantalla, severidad del incidente y rango de tiempo.
- **Builds Automatizados**: Incluye tareas personalizadas de Gradle para facilitar el proceso de compilación y prueba.

## 📚 Estructura de Módulos

- **/app**: Una aplicación de ejemplo que consume el SDK y demuestra su funcionalidad, incluyendo la UI de visualización de datos construida con Jetpack Compose.
- **/observability-sdk**: El corazón de la librería, dividido en:
  - **:presentation**: Expone la API pública del SDK (`ContractObservabilityApi`) y contiene la lógica del ViewModel que sirve de puente con la capa de dominio.
  - **:domain**: Contiene la lógica de negocio pura, las interfaces de los repositorios y los casos de uso (Use Cases).
  - **:data**: Implementa los repositorios de la capa de dominio, manejando las fuentes de datos (Room para la base de datos local y Ktor para el cliente de red).
- **/build-logic**: Centraliza la lógica de compilación de Gradle para mantener los `build.gradle.kts` de cada módulo limpios y consistentes.

## 🚀 Cómo Compilar

Este proyecto incluye tareas personalizadas de Gradle para facilitar la compilación de los artefactos de la librería.

### Construir los AARs de Desarrollo

Para ejecutar las pruebas unitarias y, si pasan, ensamblar los artefactos `.aar` de la variante `dev` para cada módulo del SDK, ejecuta la siguiente tarea desde la raíz del proyecto:

```bash
./gradlew buildDevAars
```

Esta tarea se encargará de:
1. Ejecutar las pruebas unitarias de los módulos `data`, `domain` y `presentation`.
2. Si todas las pruebas son exitosas, ensamblará los AARs de la variante `dev`.

## 🛠️ Cómo Usar el SDK (Ejemplo en la App)

La interacción con el SDK desde una aplicación cliente (como `MainActivity`) se realiza a través del contrato `ContractObservabilityApi`.

1. **Inyectar la API del SDK**:
   Usa Koin para obtener una instancia del contrato en tu Composable o Activity.

   ```kotlin
   val sdk: ContractObservabilityApi = koinViewModel()
   ```

2. **Observar el Estado**:
   Recolecta el `state` como un `StateFlow` para que tu UI reaccione a los cambios.

   ```kotlin
   val state by sdk.state.collectAsStateWithLifecycle()
   ```

3. **Enviar Eventos (Acciones)**:
   Usa la función `onEvent` para enviar acciones al SDK, como registrar una nueva pantalla o actualizar un filtro.

   ```kotlin
   // Añadir una nueva pantalla
   sdk.onEvent(MainActions.InsertScreen("LoginScreen"))

   // Actualizar el filtro de tiempo
   sdk.onEvent(MainActions.FilterByTime(TimeFilter.Last30Minutes))
   ```

## 💻 Pila Tecnológica

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose
- **Inyección de Dependencias**: Koin
- **Base de Datos**: Room
- **Red**: Ktor
- **Pruebas**: JUnit 4, MockK, Turbine
- **Automatización**: Gradle
