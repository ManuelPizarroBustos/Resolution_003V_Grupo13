# TechRent (Ventas y Arriendos) - Offline

Proyecto Android Studio (Kotlin + Jetpack Compose + Material 3 + Room + Navigation + DataStore).
Funciona 100% offline (Room/DataStore) y usa recursos nativos: Cámara (Activity Result) + GPS (LocationManagerCompat).

## Requisitos
- Android Studio Koala o superior
- JDK 17
- Gradle se sincroniza vía Android Studio (wrapper URL está configurada).

> Nota: este zip no incluye `gradle-wrapper.jar` (Android Studio lo re-genera/descarga al sincronizar).
> Si tu AS lo pide explícitamente, ejecuta: **Tools > Gradle > Generate Gradle Wrapper** o crea un proyecto Compose nuevo y copia el `gradle/wrapper/gradle-wrapper.jar`.

## Credenciales seed (primer arranque)
- Admin: admin@demo.cl / Admin123!
- Cliente: cliente@demo.cl / Cliente123!

## Flujos
- Cliente: Home -> Detalle -> Carrito -> Checkout (GPS opcional) -> Historial -> Detalle
- Admin: Items (CRUD + Cámara) -> Órdenes (lista + detalle + cambio estado)


## Nota: Auto-login
La app guarda sesión en DataStore. Si ya iniciaste sesión una vez, Splash saltará el Login. Para volver a ver Login, usa "Salir" o borra datos de la app (Settings > Apps > TechRent > Storage > Clear).
