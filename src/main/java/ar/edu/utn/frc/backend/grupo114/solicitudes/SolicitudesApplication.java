package ar.edu.utn.frc.backend.grupo114.solicitudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Habilita Feign para comunicación con otros microservicios
public class SolicitudesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SolicitudesApplication.class, args);
        System.out.println("\n" +
            "╔═══════════════════════════════════════════════════════════╗\n" +
            "║  🚀 Microservicio de Solicitudes iniciado correctamente  ║\n" +
            "║  📦 Puerto: 8084                                          ║\n" +
            "║  📚 Swagger: http://localhost:8084/swagger-ui.html       ║\n" +
            "╚═══════════════════════════════════════════════════════════╝\n");
    }
}