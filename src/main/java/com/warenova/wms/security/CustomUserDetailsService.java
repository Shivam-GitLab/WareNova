package com.warenova.wms.security;

import com.warenova.wms.modules.auth.entity.User;
import com.warenova.wms.modules.auth.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// ================================================
// CUSTOM USER DETAILS SERVICE
// ================================================
// PURPOSE:
// Tells Spring Security HOW to load a user
// from OUR database during authentication
//
// Spring Security calls this automatically
// during login and JWT token validation
//
// IMPLEMENTS UserDetailsService:
// This is a Spring Security interface with
// ONE method: loadUserByUsername()
// We MUST implement this method
//
// HOW IT CONNECTS:
// LoginRequest
//      ↓
// AuthenticationManager
//      ↓
// DaoAuthenticationProvider
//      ↓
// CustomUserDetailsService.loadUserByUsername()
//      ↓
// UserRepository.findByUsername() → DB query
//      ↓
// Returns UserDetails to Spring Security
//      ↓
// Spring verifies password automatically
//      ↓
// ✅ Login success or ❌ Login failed
// ================================================

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    // ── Access user data from database ───────────

    private final UserRepository userRepository;

    // ================================================
    // LOAD USER BY USERNAME
    // ================================================
    // Called by Spring Security automatically:
    // 1. During login → verify username exists
    // 2. During JWT filter → verify user still exists
    //
    // Steps:
    // 1. Search user in database by username
    // 2. If not found → throw exception
    // 3. If found → build UserDetails object
    // 4. Return UserDetails to Spring Security
    //
    // Spring Security then:
    // → Compares provided password with stored hash
    // → Checks if account is active/enabled
    // → Loads roles/authorities
    // ================================================
    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // ── Find user in database ────────────────
        // If not found → throw exception
        // Spring Security catches this and
        // returns 401 Unauthorized to client
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with username: "
                                        + username
                        )
                );

        // ── Check if user account is active ──────
        // Inactive users cannot login
        if (!user.getActive()) {
            throw new UsernameNotFoundException(
                    "User account is inactive: " + username
            );
        }

        // ================================================
        // BUILD AND RETURN SPRING USERDETAILS OBJECT
        // ================================================
        // Spring Security needs UserDetails object with:
        // 1. username    → for identification
        // 2. password    → for verification (BCrypt hash)
        // 3. authorities → roles & permissions
        //
        // SimpleGrantedAuthority wraps our role string
        // Example: new SimpleGrantedAuthority("ROLE_ADMIN")
        // ================================================
        return org.springframework.security.core
                .userdetails.User
                .withUsername(user.getUsername())
                // BCrypt hashed password
                // Spring will compare with provided password
                .password(user.getPassword())
                // Convert role string to GrantedAuthority
                // Example: "ROLE_ADMIN" → SimpleGrantedAuthority
                .authorities(List.of(
                        new SimpleGrantedAuthority(user.getRole())
                ))
                .build();
    }
}