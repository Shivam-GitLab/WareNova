package com.warenova.wms.modules.auth.repository;

import com.warenova.wms.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

// ================================================
// USER REPOSITORY
// ================================================
// PURPOSE:
// All database operations for User entity
//
// EXTENDS JpaRepository<User, Long>:
// User = entity class
// Long = primary key type
//
// JpaRepository gives us FREE methods:
// save()        → insert or update
// findById()    → find by primary key
// findAll()     → get all records
// deleteById()  → delete by primary key
// count()       → count total records
// existsById()  → check if record exists
//
// We add CUSTOM methods below for
// WMS specific queries
// ================================================

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    // ================================================
    // FIND BY USERNAME
    // ================================================
    // Used by:
    // CustomUserDetailsService → during login
    // JwtAuthFilter → on every request
    //
    // Optional = may or may not find user
    // Caller must handle empty case
    // ================================================
    Optional<User> findByUsername(String username);

    // ================================================
    // FIND BY EMAIL
    // ================================================
    // Used during registration
    // to check if email already exists
    // ================================================
    Optional<User> findByEmail(String email);

    // ================================================
    // EXISTS BY USERNAME
    // ================================================
    // Faster than findByUsername()
    // Just checks existence — no data loaded
    // Used in registration validation
    // ================================================
    boolean existsByUsername(String username);

    // ================================================
    // EXISTS BY EMAIL
    // ================================================
    // Used in registration validation
    // Check before creating new user
    // ================================================
    boolean existsByEmail(String email);

    // ================================================
    // UPDATE LAST LOGIN
    // ================================================
    // Called after every successful login
    // Updates last_login_at timestamp
    //
    // @Modifying = required for UPDATE queries
    // @Query = custom JPQL query
    // @Param = bind method param to query param
    // ================================================
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.username = :username")
    void updateLastLogin(
            @Param("username") String username,
            @Param("loginTime") LocalDateTime loginTime
    );

    // ================================================
    // FIND BY USERNAME AND ACTIVE
    // ================================================
    // Find only active users by username
    // Inactive users cannot login
    // ================================================
    Optional<User> findByUsernameAndActiveTrue(
            String username
    );
}