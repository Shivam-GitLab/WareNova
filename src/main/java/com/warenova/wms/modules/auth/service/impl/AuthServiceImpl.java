package com.warenova.wms.modules.auth.service;

import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.modules.auth.dto.AuthResponse;
import com.warenova.wms.modules.auth.dto.LoginRequest;
import com.warenova.wms.modules.auth.dto.RegisterRequest;
import com.warenova.wms.modules.auth.entity.User;
import com.warenova.wms.modules.auth.repository.UserRepository;
import com.warenova.wms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// ================================================
// AUTH SERVICE IMPLEMENTATION
// ================================================
// PURPOSE:
// Actual business logic for login & register
//
// @Service → tells Spring this is a bean
//   Spring will create an instance of this class
//   and inject it wherever AuthService is needed
//   THIS is why the bean error was occurring!
//   Without @Service → Spring cannot find the bean
//
// @Slf4j → gives us log.info() log.error() etc
//   log.info("message") prints to console
//
// @RequiredArgsConstructor → Lombok generates
//   constructor for all FINAL fields
//   replaces writing @Autowired on each field
//   cleaner and recommended in Spring Boot 3+
//
// IMPLEMENTS AuthService:
//   Must provide login() and register() methods
//   Defined in AuthService interface
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // ================================================
    // DEPENDENCIES
    // ================================================
    // All final → injected via constructor
    // @RequiredArgsConstructor handles injection
    // ================================================

    // ── Access users table in database ───────────
    private final UserRepository userRepository;

    // ── BCrypt password encoding ─────────────────
    // Encode on register
    // Spring auto verifies on login
    private final PasswordEncoder passwordEncoder;

    // ── Triggers authentication process ──────────
    // Calls CustomUserDetailsService internally
    // Verifies username + password
    private final AuthenticationManager authManager;

    // ── Generate JWT token after login ───────────
    private final JwtUtil jwtUtil;

    // ================================================
    // LOGIN
    // ================================================
    // FLOW:
    //
    // 1. authManager.authenticate()
    //    → calls CustomUserDetailsService
    //       .loadUserByUsername()
    //    → loads user from DB
    //    → BCrypt compares passwords
    //    → throws BadCredentialsException if wrong
    //    → GlobalExceptionHandler returns 401
    //
    // 2. Load full User object from DB
    //    (for building response)
    //
    // 3. Update last login timestamp in DB
    //
    // 4. Generate JWT token with username + role
    //
    // 5. Build and return AuthResponse
    //    with token + user details
    //
    // @Transactional → wraps in DB transaction
    // If anything fails → DB changes rolled back
    // ================================================
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {

        // ── Log login attempt ─────────────────────
        log.info("Login attempt for username: {}",
                request.getUsername());

        // ================================================
        // STEP 1: AUTHENTICATE CREDENTIALS
        // ================================================
        // This ONE line does everything:
        // → Finds user in DB by username
        // → Checks if account is active
        // → Compares BCrypt password hash
        // → If wrong → throws BadCredentialsException
        //   → GlobalExceptionHandler catches it
        //   → Returns 401 Unauthorized to client
        // ================================================
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),  // username
                        request.getPassword()   // plain text password
                        // Spring BCrypt compares internally ✅
                )
        );

        // ================================================
        // STEP 2: LOAD USER FROM DATABASE
        // ================================================
        // Authentication passed ✅
        // Now load full User entity for response data
        // orElseThrow() = safe — user exists (just verified)
        // ================================================
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found after authentication"
                        )
                );

        // ================================================
        // STEP 3: UPDATE LAST LOGIN TIMESTAMP
        // ================================================
        // Track when user last logged in
        // Useful for security audit
        // ================================================
        userRepository.updateLastLogin(
                user.getUsername(),
                LocalDateTime.now()
        );

        // ================================================
        // STEP 4: GENERATE JWT TOKEN
        // ================================================
        // Token contains:
        // subject  → username
        // claim    → role (ROLE_ADMIN etc)
        // issuedAt → now
        // expiry   → now + 24 hours
        // ================================================
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );

        // ── Log success ───────────────────────────
        log.info("Login successful for username: {}",
                request.getUsername());

        // ================================================
        // STEP 5: BUILD AND RETURN RESPONSE
        // ================================================
        // Return token + user details to client
        // Client stores token for future requests
        // ================================================
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .warehouseCode(user.getWarehouseCode())
                // 86400000ms = 24 hours
                .expiresIn(86400000L)
                .build();
    }

    // ================================================
    // REGISTER
    // ================================================
    // FLOW:
    //
    // 1. Check username not already taken
    //    → throws DuplicateResourceException if taken
    //    → GlobalExceptionHandler returns 409 Conflict
    //
    // 2. Check email not already taken
    //    → throws DuplicateResourceException if taken
    //
    // 3. Build User entity
    //    → BCrypt encode password (NEVER plain text!)
    //    → Set active = true by default
    //
    // 4. Save user to database
    //
    // 5. Generate JWT token
    //
    // 6. Return token + user details
    //    (user is auto logged in after register)
    //
    // @Transactional → if save fails
    // nothing is committed to DB
    // ================================================
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // ── Log registration attempt ──────────────
        log.info("Register attempt for username: {}",
                request.getUsername());

        // ================================================
        // STEP 1: CHECK USERNAME NOT TAKEN
        // ================================================
        // existsByUsername() = fast DB check
        // No need to load full User object
        // ================================================
        if (userRepository.existsByUsername(
                request.getUsername())) {

            // Throws 409 Conflict
            // GlobalExceptionHandler handles it
            throw new DuplicateResourceException(
                    "User",
                    "username",
                    request.getUsername()
            );
        }

        // ================================================
        // STEP 2: CHECK EMAIL NOT TAKEN
        // ================================================
        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "User",
                    "email",
                    request.getEmail()
            );
        }

        // ================================================
        // STEP 3: BUILD USER ENTITY
        // ================================================
        // Use Builder pattern (from @Builder in User.java)
        // ================================================
        User user = User.builder()
                .username(request.getUsername())

                // ── ALWAYS encode password! ──────────
                // passwordEncoder.encode() → BCrypt hash
                // Example: "Admin@1234" →
                // "$2a$10$xK9zRJ8mP7tQK..."
                // NEVER save plain text password!
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .phone(request.getPhone())
                .warehouseCode(request.getWarehouseCode())

                // New users always start as active
                .active(true)
                .build();

        // ================================================
        // STEP 4: SAVE TO DATABASE
        // ================================================
        // JPA save() → INSERT SQL executed
        // Returns saved entity with generated ID
        // created_at, updated_at auto set by BaseEntity
        // created_by, updated_by auto set by AuditConfig
        // ================================================
        User savedUser = userRepository.save(user);

        // ================================================
        // STEP 5: GENERATE JWT TOKEN
        // ================================================
        String token = jwtUtil.generateToken(
                savedUser.getUsername(),
                savedUser.getRole()
        );

        // ── Log success ───────────────────────────
        log.info("User registered successfully: {}",
                request.getUsername());

        // ================================================
        // STEP 6: RETURN TOKEN + USER DETAILS
        // ================================================
        // User is automatically logged in
        // after successful registration
        // ================================================
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(savedUser.getUsername())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .warehouseCode(savedUser.getWarehouseCode())
                .expiresIn(86400000L)
                .build();
    }
}
