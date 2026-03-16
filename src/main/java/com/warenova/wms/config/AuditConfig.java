package com.warenova.wms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

// ================================================
// AUDIT CONFIG
// ================================================
// PURPOSE:
// Activates Spring JPA Auditing system
// Tells Spring WHO is currently logged in
// So BaseEntity can fill createdBy & updatedBy
//
// @EnableJpaAuditing → turns on JPA auditing
//   auditorAwareRef → points to our bean below
//   that returns current logged in username
//
// HOW IT WORKS:
// 1. User logs in → JWT token created
// 2. User calls any API
// 3. JwtAuthFilter reads token → sets
//    SecurityContext with username
// 4. AuditConfig reads SecurityContext
// 5. Returns username to Spring Auditing
// 6. Spring fills createdBy / updatedBy
//    automatically in BaseEntity
// ================================================

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    // ================================================
    // AUDITOR PROVIDER BEAN
    // ================================================
    // Returns currently logged in username
    // Spring uses this to fill:
    //   created_by column
    //   updated_by column
    // in ALL entities that extend BaseEntity
    // ================================================
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {

            // ================================================
            // GET CURRENT AUTHENTICATION
            // from Spring Security context holder
            // SecurityContext is set by JwtAuthFilter
            // when user makes any authenticated request
            // ================================================
            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            // ================================================
            // CHECK IF USER IS AUTHENTICATED
            // If no user logged in (like public APIs)
            // return "SYSTEM" as default auditor
            // ================================================
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getName()
                    .equals("anonymousUser")) {
                return Optional.of("SYSTEM");
            }

            // ================================================
            // RETURN LOGGED IN USERNAME
            // This username will be stored in:
            // created_by and updated_by columns
            // ================================================
            return Optional.of(authentication.getName());
        };
    }
}