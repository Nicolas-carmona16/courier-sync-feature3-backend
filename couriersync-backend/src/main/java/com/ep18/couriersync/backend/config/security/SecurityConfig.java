package com.ep18.couriersync.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // para @PreAuthorize si la usas
public class SecurityConfig {

    @Bean
    @Profile("!dev-open") // seguridad real (local/dev/prod)
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            JwtAuthenticationConverter jwtAuthConverter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // expón metrics solo si es necesario (observabilidad)
                        .requestMatchers("/actuator/metrics").permitAll()
                        .requestMatchers("/graphiql/**").hasAnyRole("DEV", "ADMIN") // solo roles altos
                        .requestMatchers(HttpMethod.OPTIONS, "/graphql").permitAll() // preflight CORS
                        .requestMatchers("/graphql").authenticated()                 // resto GraphQL con JWT
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }

    /** Convierte realm roles de Keycloak en GrantedAuthorities con prefijo ROLE_ */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        // Si quieres que el principal sea el username visible:
        // converter.setPrincipalClaimName("preferred_username");
        return converter;
    }

    /** Cadena abierta SOLO para pruebas sin IdP: activar con SPRING_PROFILES_ACTIVE=dev-open */
    @Bean
    @Profile("dev-open")
    SecurityFilterChain devOpenChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
