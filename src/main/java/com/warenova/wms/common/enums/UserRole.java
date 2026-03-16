package com.warenova.wms.common.enums;

// ================================================
// USER ROLE ENUM
// ================================================
// Defines all roles in WareNova WMS system
// Used in:
//   User entity → role field
//   SecurityConfig → access rules
//   JWT token → role claim
//
// ROLE HIERARCHY:
// ADMIN       → can do everything
// SUPERVISOR  → can view & approve
// WAREHOUSE   → warehouse operations
// BILLING     → billing & invoices
// REPORT      → view reports only
// ================================================
public enum UserRole {

    // ── Full system access ──────────────────────
    // Can create users, configure system,
    // access all modules
    ROLE_ADMIN,

    // ── Warehouse operations access ─────────────
    // Can do inbound, inventory, outbound
    // Cannot access billing or admin settings
    ROLE_WAREHOUSE,

    // ── Supervisor access ───────────────────────
    // Can view all operations
    // Can approve transactions
    // Cannot configure system
    ROLE_SUPERVISOR,

    // ── Billing access ──────────────────────────
    // Can manage 3PL contracts
    // Can generate invoices
    // Cannot access warehouse operations
    ROLE_BILLING,

    // ── Report only access ──────────────────────
    // Can only view reports & dashboards
    // Cannot modify any data
    ROLE_REPORT
}