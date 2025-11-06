package ar.edu.utn.frc.backend.grupo114.solicitudes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicializa el microservicio de Solicitudes.
 * 
 * Marca el punto de entrada de la aplicación Spring Boot.
 * Al ejecutarse, levanta el contexto de Spring y expone los endpoints REST.
 */
@SpringBootApplication
public class SolicitudesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolicitudesApplication.class, args);
        System.out.println("✅ Microservicio de Solicitudes iniciado correctamente...");
    }
}
