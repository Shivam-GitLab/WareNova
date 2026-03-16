package com.warenova.wms.common.enums;

// ================================================
// WAREHOUSE STATUS ENUM
// ================================================
// Defines operational status of a warehouse
// Used in:
//   Warehouse entity → status field
//   All transactions → check warehouse is ACTIVE
// ================================================
public enum WarehouseStatus {

    // ── Warehouse is fully operational ──────────
    // All operations allowed
    ACTIVE,

    // ── Warehouse is closed ─────────────────────
    // No operations allowed
    // Historical data still accessible
    INACTIVE,

    // ── Warehouse under maintenance ─────────────
    // Temporarily closed
    // No new operations allowed
    // Existing orders being completed
    UNDER_MAINTENANCE
}