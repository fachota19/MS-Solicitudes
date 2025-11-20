package ar.edu.utn.frc.backend.grupo114.solicitudes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth

                // ---------------------------------------
                //     ENDPOINTS SEGÚN SWAGGER
                // ---------------------------------------

                // Cliente: creación de solicitud
                .requestMatchers("/solicitudes").hasRole("CLIENTE")
                .requestMatchers("/solicitudes/**").authenticated()

                // Seguimiento de contenedor (Cliente u Operador)
                .requestMatchers("/solicitudes/*/seguimiento")
                .hasAnyRole("CLIENTE", "OPERADOR")

                // Tramos: inicio / fin (Transportista)
                .requestMatchers("/tramos/*/iniciar").hasRole("TRANSPORTISTA")
                .requestMatchers("/tramos/*/finalizar").hasRole("TRANSPORTISTA")

                // Operador: ver todas las solicitudes
                .requestMatchers("/solicitudes").hasRole("OPERADOR")

                // Swagger libre
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs/**"
                ).permitAll()

                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth.jwt());

        return http.build();
    }
}
