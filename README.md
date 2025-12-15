# Fly Transportation - Backend (Spring Boot)

## Repositorio:
https://github.com/GermanelGris/FlyTMovilBack.git

Backend desarrollado en Java con Spring Boot para la aplicación Android “Registro Fly Transportation”.
Gestiona autenticación mediante JWT, usuarios, vuelos y un panel administrativo con roles (USER, ADMIN).
Permite además la subida y el servicio de archivos desde la carpeta uploads/.

## Contenido

Descripción

Requisitos previos

Instalación y ejecución

Variables de entorno

Endpoints principales

Swagger / Documentación API

Notas importantes

Estructura del proyecto

Autores

## Descripción

API REST que expone endpoints para:

Autenticación de usuarios

Gestión de usuarios

Operaciones CRUD de vuelos

Incluye:

Control de acceso basado en roles

Subida y servicio de archivos estáticos

Configuración de CORS en
src/main/java/.../config/WebConfig.java

## Requisitos previos

Java 17 o superior

Maven 3.6 o superior

Base de datos SQL (PostgreSQL o MySQL) o H2 para desarrollo

npm (solo si existen scripts o assets adicionales)

## Instalación
Clonar el repositorio
git clone https://github.com/GermanelGris/FlyTMovilBack.git
cd FlyTMovilBack

Compilar y ejecutar
mvn clean package
mvn spring-boot:run


Opcional (si existen scripts adicionales):

npm run dev

## Variables de entorno (ejemplo)

Configurar en application.properties, application.yml o como variables de entorno:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/flytbd
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=clave
JWT_SECRET=clave_secreta_para_jwt
SERVER_PORT=8090


Por defecto, la aplicación corre en el puerto 8090 (configurable).

## Endpoints principales
## Autenticación
### Registro de usuario
POST /api/auth/register


Body (JSON):

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "password": "secreto",
  "roles": ["USER"]
}


Nota:
El campo roles debe enviarse como un arreglo (ejemplo: ["USER", "ADMIN"]).

### Login
POST /api/auth/login


Body:

{
  "email": "juan@example.com",
  "password": "secreto"
}


Respuesta típica:

{
  "token": "token_jwt",
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",
  "id": 1,
  "fotoPerfil": "/uploads/archivo.jpg"
}

### Perfil del usuario autenticado
GET /api/auth/me


Headers:

Authorization: Bearer <token>


Devuelve la información del usuario autenticado.

### Vuelos
GET /api/vuelos


Lista o filtra vuelos (público o según lógica definida).

POST /api/vuelos


Crea un vuelo (requiere rol ADMIN).

PUT /api/vuelos/{id}


Edita un vuelo (requiere rol ADMIN).

DELETE /api/vuelos/{id}


Elimina un vuelo (requiere rol ADMIN).

Archivos subidos

Los archivos se sirven públicamente desde:

GET /uploads/**


Configuración ubicada en:
src/main/java/.../config/WebConfig.java

La variable UPLOADS_DIR debe apuntar a la carpeta física donde se almacenan los archivos.

## Notas importantes

Revisar WebConfig.java para ajustar allowedOrigins en addCorsMappings.

Si la aplicación Android utiliza emulador, apuntar a:
http://10.0.2.2:8090/

Verificar que GET /api/auth/me retorne correctamente:

id

email

roles

fotoPerfil

El JWT debe incluir exp y sub, y validarse en los filtros de seguridad.

Los endpoints administrativos deben protegerse por roles en la configuración de seguridad.

Asegurar permisos de lectura y escritura en la carpeta UPLOADS_DIR.

Para depuración, activar logs:

logging.level.org.springframework.web=DEBUG

Estructura del proyecto (resumen)
src/main/java/.../config/        Configuraciones (CORS, WebConfig, seguridad)
src/main/java/.../controller/    Controladores REST (AuthController, VuelosController)
src/main/java/.../service/       Lógica de negocio
src/main/java/.../repository/    Repositorios JPA
src/main/resources/              application.properties / application.yml
uploads/                         Archivos subidos

## Autores

Contribuidores y responsables del proyecto:

Francisca Blanchard

Germán Maraboli
