package com.warenova.wms.security;

import com.warenova.wms.common.constants.WMSConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// ================================================
// JWT AUTHENTICATION FILTER
// ================================================
// PURPOSE:
// Intercepts EVERY HTTP request that comes
// to WareNova WMS and checks if it has a
// valid JWT token in the header
//
// This filter runs BEFORE any controller code
//
// HOW IT WORKS (step by step):
// 1. Request comes in
//    GET /api/inventory/items
//    Authorization: Bearer eyJobGci...
//
// 2. Filter extracts token from header
//    token = "eyJobGci..."
//
// 3. Filter extracts username from token
//    username = "admin"
//
// 4. Filter loads user from database
//    user = UserDetails(admin, ROLE_ADMIN)
//
// 5. Filter validates token
//    valid = true
//
// 6. Filter sets authentication in context
//    SecurityContext.auth = admin (authenticated)
//
// 7. Request continues to controller ✅
//
// IF TOKEN MISSING OR INVALID:
// Request continues but SecurityContext is empty
// Spring Security then returns 401 Unauthorized
//
// EXTENDS OncePerRequestFilter:
// Guarantees filter runs ONLY ONCE per request
// (not multiple times for forwarded requests)
// ================================================

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // ── JWT operations (generate, validate, extract)
    @Autowired
    private JwtUtil jwtUtil;

    // ── Load user details from database
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // ================================================
    // MAIN FILTER METHOD
    // ================================================
    // Called automatically on every request
    // by Spring Security filter chain
    // ================================================
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ================================================
        // STEP 1: GET AUTHORIZATION HEADER
        // ================================================
        // Every authenticated request must have:
        // Authorization: Bearer eyJhbGciOiJIUzI1...
        //
        // If header is missing → skip JWT processing
        // Spring Security will handle as unauthenticated
        // ================================================
        String authHeader = request.getHeader(
                WMSConstants.AUTH_HEADER);

        // ── Initialize variables ─────────────────────
        String token = null;
        String username = null;

        // ================================================
        // STEP 2: EXTRACT TOKEN FROM HEADER
        // ================================================
        // Header format: "Bearer eyJhbGci..."
        // We need only: "eyJhbGci..."
        // So we skip first 7 characters ("Bearer ")
        // ================================================
        if (authHeader != null
                && authHeader.startsWith(
                WMSConstants.BEARER_PREFIX)) {

            // Remove "Bearer " prefix → get token
            token = authHeader.substring(7);

            // Extract username from token payload
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Token is malformed or invalid
                // Continue filter chain
                // Spring Security will return 401
                logger.warn("Could not extract username from token: "
                        + e.getMessage());
            }
        }

        // ================================================
        // STEP 3: VALIDATE TOKEN AND SET AUTHENTICATION
        // ================================================
        // Only proceed if:
        // 1. Username was extracted from token
        // 2. No authentication already set
        //    (avoid processing twice)
        // ================================================
        if (username != null
                && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            // ── Load full user details from database ──
            // This checks if user still exists and active
            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            // ── Validate the token ───────────────────
            // Check: not expired, valid signature
            if (jwtUtil.validateToken(token)) {

                // ================================================
                // STEP 4: CREATE AUTHENTICATION OBJECT
                // ================================================
                // UsernamePasswordAuthenticationToken =
                // Spring's authentication object
                //
                // Parameters:
                // 1. userDetails → who is authenticated
                // 2. null        → credentials (not needed after auth)
                // 3. authorities → user's roles/permissions
                // ================================================
                UsernamePasswordAuthenticationToken
                        authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // ── Add request details to auth token ────
                // Stores IP address and session info
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // ================================================
                // STEP 5: SET AUTHENTICATION IN SECURITY CONTEXT
                // ================================================
                // SecurityContextHolder stores current
                // authenticated user for this request
                //
                // After this line:
                // → User is considered authenticated
                // → @PreAuthorize annotations work
                // → AuditConfig can get username
                // → Principal available in controllers
                // ================================================
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // ================================================
        // STEP 6: CONTINUE FILTER CHAIN
        // ================================================
        // Pass request to next filter or controller
        // MUST always call this or request will hang!
        // ================================================
        filterChain.doFilter(request, response);
    }
}