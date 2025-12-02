# AppZonaGamer

## 1. Descripción general

AppZonaGamer es una aplicación Android desarrollada en Kotlin usando Jetpack Compose y Material 3.
Permite registrar usuarios gamers y guardar sus datos localmente, además de obtener información
de una API externa (RandomUser) para sugerir una ciudad.

## 2. Tecnologías utilizadas

- Lenguaje: Kotlin
- UI: Jetpack Compose + Material 3
- Arquitectura: MVVM
- Gestión de estado: ViewModel + StateFlow
- Navegación: Navigation Compose
- Almacenamiento local: Room
- Consumo de API externa: Retrofit + RandomUser API
- Corrutinas: Kotlin Coroutines
- Pruebas unitarias: JUnit 4

## 3. Arquitectura

La app sigue el patrón MVVM:

- **UI (Compose)**:
  - `RegistroScreen`: formulario de registro.
  - `ResumenScreen`: muestra el resumen de los datos ingresados y la ciudad sugerida.
- **ViewModel**:
  - `UsuarioViewModel`: expone `StateFlow<UsuarioUiState>` y maneja validación, registro y consumo de API.
- **Capa de datos**:
  - Room:
    - `UsuarioEntity`, `UsuarioDao`, `AppDatabase`.
  - Repositorio:
    - `UserRepository`: orquesta Room y la API externa.
  - API externa:
    - `RestDataSource` (Retrofit) → RandomUser API.
  - DI simple:
    - `DataSourceModule`: construye `Room`, `Retrofit` y `UserRepository`.

## 4. Flujo de Navegación

1. La app inicia en `RegistroScreen`.
2. El usuario completa el formulario y presiona **Registrar**:
   - Se valida el formulario (`validarFormulario()`).
   - Si es válido, se guarda en Room (`guardarUsuarioLocal`) y se navega a `ResumenScreen`.
3. En `ResumenScreen` se muestran los datos y la ciudad sugerida.
4. Desde `RegistroScreen` el usuario puede también presionar
   **“Sugerir ciudad desde API externa”**:
   - Se llama a la API RandomUser usando Retrofit.
   - La ciudad recibida se guarda en el estado y se muestra en el resumen.

## 5. Validación de formularios

Las reglas de validación se implementan en `UsuarioViewModel.validarFormulario()`:

- Nombre: obligatorio, solo letras y espacios, máx 100 caracteres.
- Correo: obligatorio, máx 60 caracteres, dominio `@duoc.cl`.
- Contraseña: debe incluir mayúscula, minúscula, número y símbolo.
- Teléfono: obligatorio.
- Favoritos: obligatorio.
- Los errores se guardan en `UsuarioErrores` y se muestran bajo los campos usando `supportingText`.

## 6. Almacenamiento local (Room)

- Entidad: `UsuarioEntity`.
- DAO: `UsuarioDao` con métodos `insertar()` y `obtenerTodos()`.
- Base de datos: `AppDatabase` con pattern singleton.
- El ViewModel llama a `UserRepository.guardarUsuarioLocal()` tras un registro válido.

## 7. Consumo de API externa

- API: `https://randomuser.me/api`.
- Cliente: Retrofit (`RestDataSource`) con métodos `getUsuarioLocation()` y `getUsuarioImagen()`.
- La función `UsuarioViewModel.cargarCiudadDesdeApi()`:
  - Llama al repositorio `obtenerCiudadRandom()`.
  - Actualiza el estado con la ciudad sugerida o un mensaje de error.
- La ciudad se muestra en `ResumenScreen`.

## 8. Pruebas unitarias

Archivo: `UsuarioViewModelTest.kt`

- Verifica que:
  - Si el nombre está vacío, `validarFormulario()` devuelve `false`.
  - Si todos los campos son correctos, devuelve `true`.

También se incluye la prueba de ejemplo `ExampleUnitTest` (2 + 2 = 4).

## 9. Generación de APK firmado

Se describe el proceso usado en Android Studio:
- Build > Generate Signed Bundle / APK > APK > ...
- Creación de keystore y generación de `app-release.apk`.
