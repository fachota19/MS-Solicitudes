# 🧱 Microservicio de Solicitudes

Microservicio perteneciente al **Sistema de Logística de Contenedores (TPI 2025 - Backend de Aplicaciones)**.  
Implementa las operaciones CRUD sobre las solicitudes de transporte y se comunica con los demás servicios del sistema (Usuarios, Tarifas, Camiones).

## ⚙️ Tecnologías
- Java 17
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Maven

## 🚀 Ejecución local
1. Crear la base de datos `solicitudesdb` en PostgreSQL.
2. Configurar las credenciales en `application.properties`.
3. Ejecutar:
   ```bash
   mvn clean install
   mvn spring-boot:run
4. API disponible en http://localhost:8081/api/solicitudes.