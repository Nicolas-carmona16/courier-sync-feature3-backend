package com.ep18.couriersync.backend.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CORS para /graphql (y opcionalmente /graphiql).
 * Soporta orígenes exactos y patrones (p.ej. https://*.vercel.app).
 *
 * Propiedad CSV: app.cors.allowed-origins
 *   - Ej: "http://localhost:3000,http://localhost:5173,https://courier-sync-feature3-frontend.vercel.app,https://*.vercel.app"
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Evita '*' por defecto si usas allowCredentials(true)
    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173,https://courier-sync-feature3-frontend.vercel.app}")
    private String allowedOriginsCsv;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> all = Arrays.stream(allowedOriginsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Separa exactos y patrones (con '*')
        String[] exact = all.stream().filter(s -> !s.contains("*")).toArray(String[]::new);
        String[] patterns = all.stream().filter(s -> s.contains("*")).toArray(String[]::new);

        // --- /graphql ---
        var reg = registry.addMapping("/graphql")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders(CorsConfiguration.ALL)
                .exposedHeaders("Content-Type") // 'Authorization' en respuesta casi nunca es necesario
                .maxAge(3600);

        if (exact.length > 0)    reg.allowedOrigins(exact);
        if (patterns.length > 0) reg.allowedOriginPatterns(patterns);

        // Si realmente necesitas cookies o auth por cookie (no por Bearer), déjalo true
        reg.allowCredentials(true);

        // --- /graphiql/** (si usas GraphiQL) ---
        var regGiql = registry.addMapping("/graphiql/**")
                .allowedMethods("GET", "OPTIONS")
                .allowedHeaders(CorsConfiguration.ALL)
                .maxAge(3600);

        if (exact.length > 0)    regGiql.allowedOrigins(exact);
        if (patterns.length > 0) regGiql.allowedOriginPatterns(patterns);
        regGiql.allowCredentials(true);
    }
}