# Prueba Técnica – API de Usuarios (Spring Boot)

Aplicación REST sencilla para registrar y listar usuarios en memoria (sin base de datos). Incluye validación de entrada, manejo global de errores y unicidad de email insensible a mayúsculas/minúsculas. El proyecto está dockerizado (multi‑stage) y provee healthcheck con Spring Boot Actuator. Se añaden pruebas unitarias e integración básicas que puedes ejecutar manualmente con Maven.

Características principales:
- Endpoints: `POST /users` y `GET /users` con validación (Jakarta Validation).
- Regla de negocio: email único (case‑insensitive) con respuesta `409 Conflict`.
- Manejo de errores uniforme (400/409) vía `GlobalExceptionHandler`.
- Dockerfile multi‑stage y `docker-compose.yml` con healthcheck.
- Actuator: `/actuator/health`, liveness y readiness probes activados.
- Pruebas sencillas (servicio, controlador y health) ejecutables a mano.

Novedades agregadas:
- Dockerización completa (Dockerfile, `.dockerignore`, `docker-compose.yml`).
- Healthcheck del contenedor apuntando a `/actuator/health`.
- Limpieza de configuración por defecto y código no esencial.
- Pruebas unitarias/integración básicas e instrucciones para ejecutarlas.

Cosas que se podrían agregar:
- Persistencia en base de datos relacional con JPA/Hibernate
- Documentación con Swagger/OpenAPI
- Autenticación y autorización con JWT u OAuth2
- Pipeline de integración continua con GitHub Actions u otra herramienta
- Pruebas automatizadas de endpoints con Testcontainers o MockMvc
- Métricas adicionales en Actuator (requests, memoria, GC)
- Cache en consultas de usuarios para optimizar rendimiento

## Endpoints

- POST `/users`
  - Request JSON:
    ```json
    {"id": 1, "name": "Juan Pérez", "email": "juan.perez@example.com"}
    ```
  - Respuestas:
    - 201 Created:
      ```json
      {"message": "Usuario registrado exitosamente."}
      ```
    - 409 Conflict (email duplicado):
      ```json
      {"error": "El email ya está registrado."}
      ```
    - 400 Bad Request (validación):
      ```json
      {"error": "campo: mensaje"}
      ```

- GET `/users`
  - Respuesta 200 OK:
    ```json
    [
      {"id": 1, "name": "Juan Pérez", "email": "juan.perez@example.com"},
      {"id": 2, "name": "Ana López", "email": "ana.lopez@example.com"}
    ]
    ```

## Ejecución (local)

Requisitos: Java 17+ y Maven.

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

## Docker

Construir la imagen y ejecutar:

```bash
docker build -t prueba-globus-cargo .
docker run --rm -p 8080:8080 prueba-globus-cargo
```

Probar:

```bash
curl http://localhost:8080/users
```

## Docker Compose

Levantar y ver estado de salud:

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f
```

El servicio declara un healthcheck que consulta `GET /actuator/health` dentro del contenedor. El estado `healthy` indica que la app está lista.

## Salud / Actuator

Se expone Actuator con el endpoint de salud:

- `GET /actuator/health` → `{ "status": "UP" }` cuando la app está sana.
- `GET /actuator/health/liveness` y `/actuator/health/readiness` disponibles (probes activados).

Notas:

- Solo `health` e `info` están expuestos por HTTP.
- Puedes ajustar memoria de la JVM con `JAVA_OPTS` (véase `docker-compose.yml`).

## Pruebas (ejecución manual)

Requisitos: Java 17+ y Maven.

Ejecutar todas las pruebas:

```bash
mvn -q test
```

Ejecutar una clase de prueba específica:

```bash
# Servicio (reglas de negocio y duplicados)
mvn -q -Dtest=com.globus.cargo.users.UserServiceTest test

# Controlador (201, 409 y 400)
mvn -q -Dtest=com.globus.cargo.users.UserControllerTest test

# Actuator health
mvn -q -Dtest=com.globus.cargo.ActuatorHealthTest test
```