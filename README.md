# Prueba Técnica – API de Usuarios (Spring Boot)

Servicio REST simple con almacenamiento en memoria para registrar usuarios y listarlos durante la sesión de ejecución.

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

- `GET /actuator/health` → `{ "status": "UP" }` cuando la app está sana
- `GET /actuator/health/liveness` y `/actuator/health/readiness` disponibles (probes activados)

Notas:

- Solo `health` e `info` están expuestos por HTTP.

