package ar.edu.utn.frc.backend.grupo114.solicitudes.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod; // Importante
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                
                // 1. Cliente crea solicitud (SOLO POST)
                // Si no pones HttpMethod.POST, esto atrapa el GET y bloquea al Operador
                .requestMatchers(HttpMethod.POST, "/api/solicitudes").hasRole("CLIENTE")

                // 2. Operador lista solicitudes (SOLO GET)
                // Aquí podrías agregar "CLIENTE" si ellos también pueden listar las suyas
                .requestMatchers(HttpMethod.GET, "/api/solicitudes").hasRole("OPERADOR")
                .requestMatchers(HttpMethod.GET, "/api/solicitudes/ruta-tentativa")
                        .hasAnyRole("CLIENTE", "OPERADOR")

                // 3. Ver seguimiento (GET específico)
                .requestMatchers(HttpMethod.GET, "/api/solicitudes/*/seguimiento")
                        .hasAnyRole("CLIENTE", "OPERADOR")
                .requestMatchers(HttpMethod.GET, "/api/solicitudes/seguimiento/**")
                        .hasAnyRole("CLIENTE", "OPERADOR")

                // 4. Transportista gestiona tramos
                .requestMatchers("/api/tramos/*/iniciar").hasRole("TRANSPORTISTA")
                .requestMatchers("/api/tramos/*/finalizar").hasRole("TRANSPORTISTA")

                // 5. Operador puede hacer todo lo demás en /solicitudes (PUT, DELETE, get by id)
                .requestMatchers("/api/solicitudes/**").hasRole("OPERADOR")

                // 6. Swagger y Docs (Público)
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api-docs/**"
                ).permitAll()

                .anyRequest().authenticated()
            )

            .oauth2ResourceServer(oauth ->
                oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return converter;
    }

    // Tu convertidor estaba bien, lo mantengo igual
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

            if (realmAccess == null || realmAccess.isEmpty())
                return List.of();

            List<String> roles = (List<String>) realmAccess.get("roles");

            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        }
    }
}
