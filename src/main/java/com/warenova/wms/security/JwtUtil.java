package com.warenova.wms.security;

import com.warenova.wms.common.constants.WMSConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// ================================================
// JWT UTILITY CLASS
// ================================================
// PURPOSE:
// Handles all JWT token operations:
// 1. Generate token after login
// 2. Extract username from token
// 3. Extract role from token
// 4. Validate token
//
// USED BY:
// AuthServiceImpl  → generateToken() after login
// JwtAuthFilter    → extractUsername(), validateToken()
// ================================================

@Component
public class JwtUtil {

    // ================================================
    // JWT SECRET KEY
    // Loaded from application.properties
    // jwt.secret=WareNova-Secret-Key-2024-...
    // ================================================
    @Value("${jwt.secret}")
    private String secret;

    // ================================================
    // JWT EXPIRATION TIME
    // Loaded from application.properties
    // jwt.expiration=86400000 (24 hours)
    // ================================================
    @Value("${jwt.expiration}")
    private Long expiration;

    // ================================================
    // STEP 1 — GET SIGNING KEY (PRIVATE HELPER)
    // ================================================
    // Must be defined FIRST because all other
    // methods below depend on this method
    //
    // Converts secret string → SecretKey object
    // Used to sign tokens when generating
    // Used to verify tokens when validating
    // ================================================
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ================================================
    // STEP 2 — EXTRACT ALL CLAIMS (PRIVATE HELPER)
    // ================================================
    // Must be defined SECOND because
    // extractUsername() and extractRole() below
    // both call this method
    //
    // Parses the JWT token string
    // Returns all payload data (claims) from token
    //
    // WHAT ARE CLAIMS?
    // Claims = data stored inside token
    // Example:
    // {
    //   "sub": "admin",          ← subject (username)
    //   "role": "ROLE_ADMIN",    ← custom claim
    //   "iat": 1705312200,       ← issued at
    //   "exp": 1705398600        ← expiration
    // }
    // ================================================
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                // Set secret key to verify signature
                .verifyWith(getSigningKey())
                .build()
                // JWT 0.12.6: parseSignedClaims()
                // replaces old parseClaimsJws()
                .parseSignedClaims(token)
                // JWT 0.12.6: getPayload()
                // replaces old getBody()
                .getPayload();
    }

    // ================================================
    // STEP 3 — EXTRACT USERNAME FROM TOKEN
    // ================================================
    // Calls extractAllClaims() → gets subject field
    // Subject = username stored during generateToken()
    //
    // CALLED BY:
    // JwtAuthFilter → to identify who is requesting
    // ================================================
    public String extractUsername(String token) {
        // getSubject() returns the "sub" claim
        // which we set as username in generateToken()
        return extractAllClaims(token).getSubject();
    }

    // ================================================
    // STEP 4 — EXTRACT ROLE FROM TOKEN
    // ================================================
    // Calls extractAllClaims() → gets role claim
    // Role = "ROLE_ADMIN", "ROLE_WAREHOUSE" etc
    //
    // CALLED BY:
    // AuthServiceImpl → to include role in response
    // ================================================
    public String extractRole(String token) {
        return extractAllClaims(token)
                .get(WMSConstants.JWT_ROLE_KEY, String.class);
    }

    // ================================================
    // STEP 5 — VALIDATE TOKEN
    // ================================================
    // Tries to parse the token
    // If parsing succeeds → token is valid
    // If any exception → token is invalid/expired
    //
    // CALLED BY:
    // JwtAuthFilter → before setting authentication
    //
    // POSSIBLE EXCEPTIONS CAUGHT:
    // ExpiredJwtException    → token lifetime ended
    // SignatureException     → token was tampered
    // MalformedJwtException  → not a valid JWT
    // ================================================
    public boolean validateToken(String token) {
        try {
            // Try to parse → throws if invalid
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            // No exception = valid token
            return true;
        } catch (Exception e) {
            // Any exception = invalid token
            return false;
        }
    }

    // ================================================
    // STEP 6 — GENERATE TOKEN
    // ================================================
    // Called AFTER successful login
    // Creates and returns signed JWT string
    //
    // TOKEN CONTAINS:
    // subject   → username
    // role      → user role
    // issuedAt  → now
    // expiration → now + 24 hours
    //
    // CALLED BY:
    // AuthServiceImpl → after verifying credentials
    // ================================================
    public String generateToken(
            String username,
            String role) {

        return Jwts.builder()
                // Set username as token subject
                .subject(username)
                // Add role as custom claim
                .claim(WMSConstants.JWT_ROLE_KEY, role)
                // Set token creation timestamp
                .issuedAt(new Date())
                // Set token expiry timestamp
                .expiration(new Date(
                        System.currentTimeMillis() + expiration
                ))
                // Sign with our secret key
                .signWith(getSigningKey())
                // Build final token string
                .compact();
    }
}