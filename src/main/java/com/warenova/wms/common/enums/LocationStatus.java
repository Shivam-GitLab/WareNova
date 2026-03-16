package com.warenova.wms.common.enums;

// ================================================
// LOCATION STATUS ENUM
// ================================================
// Defines current stock status of a location
// Used in:
//   Location entity → status field
//   Putaway → find EMPTY locations
//   Picking → find OCCUPIED locations
// ================================================
public enum LocationStatus {

    // ── Location has no stock ───────────────────
    // Available for putaway
    EMPTY,

    // ── Location has some stock ─────────────────
    // Has items but not completely full
    // Can accept more stock
    OCCUPIED,

    // ── Location is completely full ─────────────
    // Cannot accept any more stock
    FULL,

    // ── Location is blocked ─────────────────────
    // Not available for any operation
    // Damaged rack or maintenance
    BLOCKED
}