# Documentación Técnica: Microservicio de Notificaciones (ms-notificaciones)

## Descripción General
El microservicio `ms-notificaciones` se encarga de recibir eventos de otros servicios y almacenar/proveer las notificaciones para el usuario final.

## Arquitectura y Tecnologías
- **Lenguaje:** Java 21
- **Framework:** Spring Boot
- **Persistencia:** PostgreSQL

## Cambios Recientes
### Versión 1.2.2
- Integración de RabbitMQ Consumer con ACK/NACK manual y Dead Letter Queue (DLQ `notificaciones.dlq`).
- Endpoint y controlador REST `RabbitAdminController` para gestión de colas, exchanges y bindings.
- Incorporación de dependencia `jackson-databind` para deserialización JSON de mensajes RabbitMQ.
- Compatibilidad con método `PATCH /api/notificaciones/{id}/leer` para marcado automático desde el perfil de usuario.

### Versión 1.1.1
- Configuración de empaquetado de Docker (`Dockerfile`) para ejecución *standalone*.
