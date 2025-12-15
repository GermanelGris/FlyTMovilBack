# FlyTMovilBack

# RegistroFlyTransportation

Aplicación nativa Android (UI con Jetpack Compose) y backend en Spring Boot.

## Estructura principal
- UI (Android, Jetpack Compose): `app/src/main/java/com/example/registroflytransportation/ui/` (pantallas en `screens/`).
- Backend (Spring Boot, Java/Maven): código en `src/main/java/com/example/flytbackend/`.
- Recursos subidos por el servidor: carpeta `uploads/` en el working directory del backend.

## Requisitos
- Java 17+ y Maven para el backend.
- Android Studio 2025.x, Android SDK y Gradle para la app.
- Opcional: Node/npm si hay herramientas front-end auxiliares.

## Ejecutar backend
- Desde la terminal: `mvn spring-boot:run` (o ejecutar la clase `Application` desde el IDE).
- Archivo de configuración CORS y recursos: `src/main/java/com/example/flytbackend/config/WebConfig.java`. Por defecto permite orígenes desde `http://localhost:5174` y sirve `/uploads/**` desde la carpeta `uploads/`.

## Ejecutar app Android
- Abrir el proyecto en Android Studio y ejecutar en un emulador o dispositivo.
- Asegurar permiso de red en `AndroidManifest.xml`:
  - `<uses-permission android:name="android.permission.INTERNET" />`
- Configurar la URL base del backend (host/puerto) en la app (constante o `BuildConfig`).
- Si usas HTTP (no HTTPS), configurar `network_security_config.xml` y referenciarlo en `AndroidManifest.xml`.

## Autores
- Francisca Blanchard
- German Maraboli
