package ar.edu.utn.frc.backend.grupo114.solicitudes.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuración global para los clientes Feign.
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(
            Duration.ofSeconds(10),  // Connection timeout
            Duration.ofSeconds(60),  // Read timeout
            true                     // followRedirects
        );
    }
}