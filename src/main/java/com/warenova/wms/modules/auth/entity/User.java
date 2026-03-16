package com.warenova.wms.modules.auth.entity;

import com.warenova.wms.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// USER ENTITY
// ================================================
// PURPOSE:
// Represents the wms_users table in PostgreSQL
// Stores all WareNova system users
//
// EXTENDS BaseEntity:
// Automatically gets these columns:
// created_at, updated_at, created_by, updated_by
//
// TABLE NAME: wms_users
// wms_ prefix = good practice in MNC projects
// Avoids conflict with reserved word "users"
// in some databases
//
// RELATIONSHIPS:
// One User → One Role (stored as String)
// No separate role table needed for now
// ================================================

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_users",
        // ── Unique constraints ──────────────────────
        // Database level uniqueness enforcement
        // Faster than application level check
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        }
)
public class User extends BaseEntity {

    // ================================================
    // PRIMARY KEY
    // ================================================
    // Auto incremented by PostgreSQL
    // IDENTITY strategy = uses DB sequence
    // ================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // USERNAME
    // ================================================
    // Unique login identifier
    // Example: "john.doe" or "admin"
    // Used in JWT token as subject
    // Cannot be null or changed after creation
    // ================================================
    @Column(
            name = "username",
            nullable = false,
            unique = true,
            length = 50
    )
    private String username;

    // ================================================
    // PASSWORD
    // ================================================
    // Bcrypt hashed password
    // NEVER store plain text password!
    // Example: "$2a$10$xK9zRJ..."
    // length = 100 because Bcrypt hash is ~60 chars
    // ================================================
    @Column(
            name = "password",
            nullable = false,
            length = 100
    )
    private String password;

    // ================================================
    // EMAIL
    // ================================================
    // Unique email address
    // Used for notifications and password reset
    // ================================================
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 100
    )
    private String email;

    // ================================================
    // FULL NAME
    // ================================================
    // Display name shown in UI
    // Example: "John Doe"
    // ================================================
    @Column(
            name = "full_name",
            nullable = false,
            length = 100
    )
    private String fullName;

    // ================================================
    // ROLE
    // ================================================
    // User's role in the system
    // Stored as String in DB
    // Example: "ROLE_ADMIN", "ROLE_WAREHOUSE"
    // Matches UserRole enum values
    // Used by Spring Security for authorization
    // ================================================
    @Column(
            name = "role",
            nullable = false,
            length = 30
    )
    private String role;

    // ================================================
    // PHONE NUMBER
    // ================================================
    // Optional contact number
    // ================================================
    @Column(
            name = "phone",
            length = 15
    )
    private String phone;

    // ================================================
    // WAREHOUSE CODE
    // ================================================
    // Which warehouse this user belongs to
    // Example: "WH001", "WH002"
    // Used to filter data by warehouse
    // ================================================
    @Column(
            name = "warehouse_code",
            length = 20
    )
    private String warehouseCode;

    // ================================================
    // ACTIVE FLAG
    // ================================================
    // true  → user can log in
    // false → user is disabled (soft delete)
    //
    // We NEVER hard delete users
    // Just set active = false
    // Audit trail is preserved
    // ================================================
    @Column(
            name = "is_active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;

    // ================================================
    // LAST LOGIN
    // ================================================
    // When did this user last login
    // Updated on every successful login
    // ================================================
    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;
}