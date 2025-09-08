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

## Ejecución

Requisitos: Java 17+ y Maven.

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

## Notas

- El almacenamiento es en memoria (se pierde al reiniciar).
- La unicidad del email se valida de forma no sensible a mayúsculas/minúsculas.
- Validaciones básicas: `id` requerido, `name` requerido, `email` con formato válido.

