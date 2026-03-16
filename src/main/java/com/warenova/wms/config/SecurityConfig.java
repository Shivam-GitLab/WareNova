package com.warenova.wms.config;

import com.warenova.wms.security.CustomUserDetailsService;
import com.warenova.wms.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RestController;

// ================================================
// SECURITY CONFIG
// ================================================
// Spring Boot 4.0 + Spring Security 6.x
//
// BREAKING CHANGE IN 4.0:
// DaoAuthenticationProvider no-args constructor
// is REMOVED → must pass UserDetailsService
// in constructor directly
// ================================================

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthFilter jwtAuthFilter;


    private final CustomUserDetailsService userDetailsService;

    // ================================================
    // SECURITY FILTER CHAIN
    // ================================================
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                // ── Disable CSRF (using JWT not cookies) ─
                .csrf(AbstractHttpConfigurer::disable)

                // ── CORS config ──────────────────────────
                .cors(cors -> cors.configure(http))

                // ── Authorization rules ──────────────────
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints — no token needed
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/actuator/health"
                        ).permitAll()

                        // Admin only
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasAuthority("ROLE_ADMIN")

                        // Master data
                        .requestMatchers(
                                "/api/master/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_SUPERVISOR"
                        )

                        // Inbound operations
                        .requestMatchers(
                                "/api/inbound/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_WAREHOUSE",
                                "ROLE_SUPERVISOR"
                        )

                        // Inventory operations
                        .requestMatchers(
                                "/api/inventory/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_WAREHOUSE",
                                "ROLE_SUPERVISOR"
                        )

                        // Outbound operations
                        .requestMatchers(
                                "/api/outbound/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_WAREHOUSE",
                                "ROLE_SUPERVISOR"
                        )

                        // Billing
                        .requestMatchers(
                                "/api/billing/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_BILLING"
                        )

                        // Reports
                        .requestMatchers(
                                "/api/reports/**"
                        ).hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_SUPERVISOR",
                                "ROLE_REPORT"
                        )

                        // All other endpoints need auth
                        .anyRequest().authenticated()
                )

                // ── Stateless session (JWT) ───────────────
                // ================================================
                // IMPORTANT FOR JWT:
                // No HTTP session created on server
                // Every request must carry JWT token
                // ================================================
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )

                // ── Our authentication provider ───────────
                .authenticationProvider(
                        authenticationProvider()
                )

                // ── JWT filter before Spring auth filter ──
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ================================================
    // AUTHENTICATION PROVIDER
    // ================================================
    // SPRING BOOT 4.0 FIX:
    // Pass UserDetailsService in constructor
    // Old setUserDetailsService() is REMOVED ❌
    // ================================================
    @Bean
    public AuthenticationProvider authenticationProvider() {

        // ── Boot 4.0: pass via constructor ───────────
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        // ── Password encoder still same ──────────────
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // ================================================
    // PASSWORD ENCODER
    // ================================================
    // BCrypt — industry standard
    // Same in Boot 3.x and 4.0 ✅
    // ================================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================================================
    // AUTHENTICATION MANAGER
    // ================================================
    // Used in AuthServiceImpl.login()
    // Same in Boot 3.x and 4.0 ✅
    // ================================================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
/*
```

        ---

        ## 📊 All Spring Boot 4.0 Breaking Changes We Fixed
```
CHANGE                          3.x → 4.0
        ──────────────────────────────────────────────
DaoAuthenticationProvider()  → DaoAuthenticationProvider
        .setUserDetailsService()      (userDetailsService) ✅

server.error                 → removed deprecated
  .include-message              properties ✅
        .include-stacktrace

springdoc version            → 2.6.0 → 2.8.6 ✅

maven-compiler-plugin        → parameters=true
required ✅*/
