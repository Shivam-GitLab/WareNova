package com.warenova.wms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// ================================================
// CORS CONFIG
// ================================================
// PURPOSE:
// Configures Cross-Origin Resource Sharing (CORS)
//
// WHAT IS CORS?
// Browser security feature that blocks requests
// from different origins (domains/ports)
//
// PROBLEM WITHOUT CORS CONFIG:
// React app running on → http://localhost:3000
// Spring API running on → http://localhost:8080
// Browser blocks request → "CORS Error"
//
// WITH CORS CONFIG:
// We tell Spring to allow requests from
// specific origins/domains
// Browser allows the requests ✅
//
// FOR PRODUCTION:
// Change allowed origins to your actual
// frontend domain (not "*")
// Example: "https://warenova-app.com"
// ================================================

@Configuration
public class CorsConfig {

    // ================================================
    // CORS CONFIGURATION SOURCE
    // ================================================
    // Defines CORS rules for the application
    // Referenced in SecurityConfig
    // ================================================
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ================================================
        // ALLOWED ORIGINS
        // ================================================
        // Which frontends can call our API
        //
        // Development:
        // localhost:3000 → React dev server
        // localhost:4200 → Angular dev server
        // localhost:5173 → Vite dev server
        //
        // Production:
        // Replace with actual domain
        // "https://warenova-app.com"
        // ================================================
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",   // React
                "http://localhost:4200",   // Angular
                "http://localhost:5173"    // Vite
        ));

        // ================================================
        // ALLOWED HTTP METHODS
        // ================================================
        // Which HTTP methods frontend can use
        // GET    → read data
        // POST   → create data
        // PUT    → update data
        // DELETE → delete data
        // PATCH  → partial update
        // ================================================
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"   // Browser preflight check
        ));


        // ================================================
        // ALLOWED HEADERS
        // ================================================
        // Which headers frontend can send
        // Authorization → JWT token
        // Content-Type  → JSON format
        // ================================================
        config.setAllowedHeaders(List.of(
                "Authorization",    // JWT Bearer token
                "Content-Type",     // application/json
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // ================================================
        // ALLOW CREDENTIALS
        // ================================================
        // Allow cookies and auth headers
        // Required for JWT to work with CORS
        // ================================================
        config.setAllowCredentials(true);

        // ================================================
        // PREFLIGHT CACHE
        // ================================================
        // How long browser caches CORS response
        // 3600 seconds = 1 hour
        // Reduces preflight OPTIONS requests
        // ================================================
        config.setMaxAge(3600L);

        // ── Apply config to all API paths ─────────
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
                "/api/**", config
        );

        return source;
    }
}