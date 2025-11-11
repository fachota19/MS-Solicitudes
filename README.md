# 📦 MS-Solicitudes

Microservicio de gestión de solicitudes de transporte de contenedores.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.2.0
- PostgreSQL
- Maven
- Docker

## 📡 Endpoints Principales

- `GET /api/solicitudes` - Listar todas las solicitudes
- `POST /api/solicitudes` - Crear nueva solicitud
- `GET /api/solicitudes/{id}` - Obtener solicitud por ID
- `GET /api/solicitudes/{id}/seguimiento` - Ver seguimiento
- `PUT /api/solicitudes/{id}/ruta/{rutaId}` - Asignar ruta
- `PUT /api/tramos/{id}/iniciar` - Iniciar tramo
- `PUT /api/tramos/{id}/finalizar` - Finalizar tramo

## 🛠️ Configuración Local

### Requisitos previos
- JDK 17+
- Maven 3.9+
- PostgreSQL 15

### Base de datos
```sql
CREATE DATABASE solicitudesdb;
```

### Ejecutar
```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8084`

## 📚 Documentación API

Swagger UI: `http://localhost:8084/swagger-ui.html`

## 🐳 Docker

### Construir imagen
```bash
docker build -t ms-solicitudes:1.0.0 .
```

### Ejecutar con Docker Compose
Desde el repositorio padre:
```bash
docker-compose up ms-solicitudes
```

## 👥 Autores

Grupo 114 - TPI Backend de Aplicaciones 2025