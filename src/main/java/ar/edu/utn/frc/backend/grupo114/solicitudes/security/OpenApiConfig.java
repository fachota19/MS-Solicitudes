package ar.edu.utn.frc.backend.grupo114.solicitudes.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema de Logística de Contenedores")
                        .version("1.0.0")
                        .description("Backend distribuido basado en microservicios (TPI 2025 - Backend de Aplicaciones).")
                        .contact(new Contact()
                                .name("Grupo 114")
                                .email("grupo114@utn.edu.ar")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("API Gateway local"),
                        new Server()
                                .url("http://localhost:8084/api")
                                .description("Servidor directo (desarrollo)")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Autenticación JWT via Keycloak")));
    }
}