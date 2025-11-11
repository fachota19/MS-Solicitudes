package ar.edu.utn.frc.backend.grupo114.solicitudes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el microservicio.
 * 
 * NOTA: Por ahora está deshabilitada la seguridad para facilitar el desarrollo.
 * Cuando integres Keycloak, descomenta las líneas correspondientes.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            
            // ========== OPCIÓN 1: SIN SEGURIDAD (para desarrollo) ==========
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // ========== OPCIÓN 2: CON KEYCLOAK JWT (comentado por ahora) ==========
            /*
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                
                // Endpoints de solicitudes
                .requestMatchers(HttpMethod.POST, "/api/solicitudes").hasAnyRole("CLIENTE", "OPERADOR")
                .requestMatchers(HttpMethod.GET, "/api/solicitudes").hasRole("OPERADOR")
                .requestMatchers(HttpMethod.GET, "/api/solicitudes/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/solicitudes/{id}/seguimiento").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/solicitudes/{id}/ruta/{rutaId}").hasRole("OPERADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/solicitudes/{id}").hasRole("OPERADOR")
                
                // Endpoints de tramos
                .requestMatchers("/api/tramos/**").hasAnyRole("TRANSPORTISTA", "OPERADOR")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            */
            
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    // Descomenta cuando uses Keycloak
    /*
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    */
}