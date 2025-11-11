# --- Etapa 1: Compilación (Build Stage) ---
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

WORKDIR /app

# Copiamos solo el pom.xml primero para aprovechar caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos la aplicación (saltando tests para acelerar)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Final Stage) ---
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copiamos el JAR compilado
COPY --from=builder /app/target/*.jar app.jar

# Exponemos el puerto
EXPOSE 8084

# Variables de entorno por defecto (pueden ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=docker

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]