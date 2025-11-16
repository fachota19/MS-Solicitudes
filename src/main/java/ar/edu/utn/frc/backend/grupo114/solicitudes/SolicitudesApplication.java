package ar.edu.utn.frc.backend.grupo114.solicitudes;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableFeignClients
public class SolicitudesApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SolicitudesApplication.class, args);
        System.out.println("\n" +
            "╔═══════════════════════════════════════════════════════════╗\n" +
            "║  🚀 Microservicio de Solicitudes iniciado correctamente   ║\n" +
            "║  📦 Puerto: 8085                                          ║\n" +
            "║  📚 Swagger: http://localhost:8085/swagger-ui.html        ║\n" +
            "║  🕐 Timezone: UTC                                         ║\n" +
            "╚═══════════════════════════════════════════════════════════╝\n");
    }
}
