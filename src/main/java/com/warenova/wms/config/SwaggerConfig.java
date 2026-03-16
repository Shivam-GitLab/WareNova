package com.warenova.wms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ================================================
// SWAGGER CONFIG
// ================================================
// PURPOSE:
// Configures Swagger UI for WareNova WMS
// Swagger = Auto generated API documentation
//
// After setup visit:
// http://localhost:8080/swagger-ui/index.html
//
// WHAT SWAGGER GIVES YOU:
// → Visual list of all APIs
// → Test APIs directly from browser
// → No need for Postman during development
// → Auto documents request/response format
// → JWT token input field for auth testing
// ================================================

@Configuration
public class SwaggerConfig {

    // ================================================
    // OPEN API CONFIGURATION
    // ================================================
    // Customizes the Swagger UI appearance
    // and adds JWT authentication support
    // ================================================
    @Bean
    public OpenAPI openAPI() {

        // ── Security scheme name ──────────────────
        // Used to reference JWT auth in Swagger UI
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                // ================================================
                // API INFORMATION
                // ================================================
                // Shows at top of Swagger UI page
                // ================================================
                .info(new Info()
                        // API title
                        .title("WareNova WMS API")
                        // API description
                        .description(
                                "Complete Warehouse Management System " +
                                        "API built with Spring Boot. " +
                                        "Covers Inbound, Inventory, " +
                                        "Outbound, Billing & Reports."
                        )
                        // API version
                        .version("v1.0.0")
                        // Contact info
                        .contact(new Contact()
                                .name("WareNova Team")
                                .email("support@warenova.com")
                        )
                )

                // ================================================
                // JWT SECURITY REQUIREMENT
                // ================================================
                // Tells Swagger all APIs need JWT by default
                // Shows lock icon on each API in Swagger UI
                // ================================================
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                // ================================================
                // JWT SECURITY SCHEME DEFINITION
                // ================================================
                // Defines HOW to pass JWT token in Swagger UI
                // Adds "Authorize" button at top of Swagger
                // User enters: Bearer eyJhbGci...
                // All API calls then include this token
                // ================================================
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        // Bearer token type
                                        .name(securitySchemeName)
                                        // Pass in HTTP header
                                        .type(SecurityScheme.Type.HTTP)
                                        // Bearer scheme
                                        .scheme("bearer")
                                        // JWT format
                                        .bearerFormat("JWT")
                                        // Location in request
                                        .in(SecurityScheme.In.HEADER)
                        )
                );
    }
}