# ================================
# STAGE 1 — BUILD
# ================================
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ================================
# STAGE 2 — RUN
# ================================
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
