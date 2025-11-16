# 📦 MS-Solicitudes

Microservicio de gestión de solicitudes de transporte de contenedores para el sistema de logística del TPI Backend de Aplicaciones 2025.

## 📋 Descripción

Este microservicio es responsable de:
- Gestionar el ciclo de vida completo de solicitudes de transporte
- Administrar contenedores y sus características (peso, volumen)
- Crear y gestionar rutas con sus respectivos tramos
- Permitir seguimiento en tiempo real del estado de las solicitudes
- Coordinar con otros microservicios (Usuarios, Tarifas, Camiones)

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.2.0**
- **PostgreSQL 15**
- **Maven 3.9+**
- **Docker & Docker Compose**
- **Spring Cloud OpenFeign** (comunicación entre microservicios)
- **Swagger/OpenAPI** (documentación)

## 📡 Endpoints Principales

### Solicitudes
- `GET /api/solicitudes` - Listar todas las solicitudes (OPERADOR)
- `POST /api/solicitudes` - Crear nueva solicitud (CLIENTE)
- `GET /api/solicitudes/{id}` - Obtener solicitud por ID
- `GET /api/solicitudes/{id}/seguimiento` - Ver seguimiento detallado (CLIENTE)
- `POST /api/solicitudes/{id}/ruta` - Crear y asignar ruta a solicitud (OPERADOR)
- `DELETE /api/solicitudes/{id}` - Eliminar solicitud (OPERADOR)

### Rutas
- `GET /api/rutas/solicitud/{id}` - Obtener ruta de una solicitud

### Tramos
- `PUT /api/tramos/{id}/iniciar` - Iniciar tramo (TRANSPORTISTA)
- `PUT /api/tramos/{id}/finalizar` - Finalizar tramo (TRANSPORTISTA)

### Contenedores
- `GET /api/contenedores` - Listar contenedores (OPERADOR)
- `GET /api/contenedores/{id}` - Obtener contenedor por ID
- `POST /api/contenedores` - Crear contenedor (OPERADOR)
- `DELETE /api/contenedores/{id}` - Eliminar contenedor (OPERADOR)

## 🗄️ Modelo de Datos

### Entidades principales:
- **Solicitud**: Representa una solicitud de transporte con origen, destino, cliente, costo estimado/real
- **Contenedor**: Características físicas (peso, volumen) del contenedor a transportar
- **Ruta**: Colección de tramos que conforman el recorrido completo
- **Tramo**: Segmento individual del viaje con fechas estimadas y reales
- **TipoEstado**: Estados posibles (PENDIENTE, EN_PROCESO, COMPLETADO)
- **TipoTramo**: Tipos de tramo (DEPÓSITO, TRASLADO, ENTREGA)

## 🛠️ Configuración y Ejecución

### 📦 Requisitos previos
- JDK 17 o superior
- Maven 3.9+
- Docker Desktop
- PostgreSQL 15 (solo para ejecución local sin Docker)

---

## 🐳 Opción 1: Ejecución con Docker (Recomendado)

### 1️⃣ Construir y levantar los servicios

```bash
# Limpiar contenedores y volúmenes anteriores
docker-compose down -v

# Construir y levantar
docker-compose up --build
```

### 2️⃣ Verificar que los servicios están corriendo

```bash
docker ps
```

Deberías ver:
- `ms-solicitudes` en puerto **8085**
- `solicitudes-db` en puerto **5436**
- `pgadmin` en puerto **5050**

### 3️⃣ Acceder a los servicios

| Servicio | URL | Credenciales |
|----------|-----|--------------|
| **Microservicio** | http://localhost:8085 | - |
| **Swagger UI** | http://localhost:8085/swagger-ui.html | - |
| **pgAdmin** | http://localhost:5050 | `admin@admin.com` / `admin` |
| **PostgreSQL** | `localhost:5436` | `postgres` / `postgres` |

### 4️⃣ Configurar pgAdmin (Primera vez)

1. Abrir http://localhost:5050
2. Login con `admin@admin.com` / `admin`
3. Click derecho en **"Servers"** → **"Register"** → **"Server"**

**Pestaña General:**
- Name: `Solicitudes DB`

**Pestaña Connection:**
```
Host name/address: solicitudes-db
Port: 5432
Maintenance database: solicitudesdb
Username: postgres
Password: postgres
☑️ Save password
```

4. Click en **"Save"**

---

## 💻 Opción 2: Ejecución Local (Sin Docker)

### 1️⃣ Configurar PostgreSQL

```sql
CREATE DATABASE solicitudesdb;
```

### 2️⃣ Configurar credenciales

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5436/solicitudesdb
spring.datasource.username=postgres
spring.datasource.password=admin  # Cambiar según tu configuración
```

### 3️⃣ Compilar y ejecutar

```bash
# Limpiar y compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8085**

---

## 🧪 Pruebas de Endpoints

### Usando el archivo HTTP (VS Code / IntelliJ)

Abre `solicitudes_endpoints.http` y ejecuta las peticiones de prueba.

### Ejemplos de uso:

**1. Crear una nueva solicitud:**
```http
POST http://localhost:8085/api/solicitudes
Content-Type: application/json

{
  "clienteId": 1,
  "contenedor": {
    "pesoKg": 3500,
    "volumenM3": 18
  },
  "origenDireccion": "Av. Corrientes 1500, CABA",
  "destinoDireccion": "Bv. Oroño 500, Rosario"
}
```

**2. Consultar seguimiento:**
```http
GET http://localhost:8085/api/solicitudes/1/seguimiento
```

**3. Iniciar un tramo:**
```http
PUT http://localhost:8085/api/tramos/1/iniciar
Content-Type: application/json

{
  "fechaHoraInicioReal": "2025-11-16T10:00:00"
}
```

---

## 🔗 Integración con otros Microservicios

Este microservicio se comunica vía **Feign Client** con:

| Microservicio | Puerto | Uso |
|---------------|--------|-----|
| **MS-Usuarios** | 8082 | Validar clientes al crear solicitudes |
| **MS-Tarifas** | 8083 | Calcular costos estimados |
| **MS-Camiones** | 8084 | Consultar camiones disponibles y validar capacidad |

### Configuración de URLs (application-docker.properties):

```properties
ms.usuarios.url=http://ms-usuarios:8082/api
ms.tarifas.url=http://ms-tarifas:8083/api
ms.camiones.url=http://ms-camiones:8084/api
```

---

## 📚 Documentación de la API

### Swagger UI
Una vez levantado el servicio, acceder a:

**http://localhost:8085/swagger-ui.html**

Aquí encontrarás:
- Todos los endpoints documentados
- Modelos de datos (schemas)
- Ejemplos de request/response
- Posibilidad de probar endpoints directamente

### OpenAPI JSON
**http://localhost:8085/api-docs**

---

## 🗂️ Estructura del Proyecto

```
MS-Solicitudes/
├── src/
│   ├── main/
│   │   ├── java/ar/edu/utn/frc/backend/grupo114/solicitudes/
│   │   │   ├── client/          # Clientes Feign (integración)
│   │   │   ├── config/          # Configuraciones (Security, CORS, OpenAPI)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── mapper/          # Conversores Entity ↔ DTO
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   └── service/         # Lógica de negocio
│   │   └── resources/
│   │       ├── application.properties           # Config local
│   │       ├── application-docker.properties    # Config Docker
│   │       ├── data.sql                         # Datos iniciales
│   │       └── logback-spring.xml              # Configuración logs
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 🔧 Comandos Útiles

### Docker

```bash
# Ver logs del microservicio
docker logs ms-solicitudes -f

# Ver logs de la base de datos
docker logs solicitudes-db -f

# Reiniciar solo el microservicio
docker-compose restart ms-solicitudes

# Detener todo
docker-compose down

# Detener y eliminar volúmenes (resetea la BD)
docker-compose down -v

# Reconstruir imagen
docker-compose up --build ms-solicitudes
```

### Maven

```bash
# Compilar sin tests
mvn clean install -DskipTests

# Solo compilar
mvn clean compile

# Ejecutar tests
mvn test

# Limpiar target
mvn clean
```

## 📊 Base de Datos

### Tablas principales:
- `solicitudes`
- `contenedores`
- `rutas`
- `tramos`
- `tipos_estado`
- `tipos_tramo`


## 📝 Logs

Los logs se almacenan en:
- **Consola**: Nivel INFO
- **Archivo**: `logs/solicitudes.log` (rotación diaria)

Configuración en: `src/main/resources/logback-spring.xml`

---

## 👥 Equipo de Desarrollo

**Grupo 114** - TPI Backend de Aplicaciones 2025  
UTN - Facultad Regional Córdoba

---

## 📄 Licencia

Este proyecto es parte del Trabajo Práctico Integrador de la materia Backend de Aplicaciones.

---

## 🔗 Links Útiles

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL Docker](https://hub.docker.com/_/postgres)
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

---

> **Última actualización:** Noviembre 2025  
> **Versión del microservicio:** 1.0.0