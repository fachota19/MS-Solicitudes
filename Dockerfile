# --- Etapa 1: Compilación (Build Stage) ---
# Usamos una imagen de Maven con Java 17 para compilar la aplicación
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo el pom.xml primero para descargar dependencias
# Esto aprovecha el caché de Docker. Si el pom.xml no cambia, no vuelve a descargar todo.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y creamos el .jar (saltando los tests)
RUN mvn package -DskipTests

# --- Etapa 2: Ejecución (Final Stage) ---
# Usamos una imagen de Java 17 ligera (solo JRE) para ejecutar la app
FROM eclipse-temurin:17-jre-focal

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos ÚNICAMENTE el .jar compilado desde la etapa "builder"
COPY --from=builder /app/target/*.jar app.jar

# El comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]