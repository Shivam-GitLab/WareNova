package com.warenova.wms.common.enums;

// ================================================
// INVENTORY STATUS ENUM
// ================================================
// Defines availability status of inventory
// Used in:
//   Inventory entity → status field
//   Order allocation → only AVAILABLE stock
//   QC process → QUARANTINE until cleared
// ================================================
public enum InventoryStatus {

    // ── Stock is available for orders ───────────
    // Can be allocated and picked
    AVAILABLE,

    // ── Stock is on hold ────────────────────────
    // Cannot be picked or shipped
    // Pending QC or investigation
    ON_HOLD,

    // ── Stock in quarantine ─────────────────────
    // Suspected damage or quality issue
    // Waiting for inspection
    QUARANTINE,

    // ── Stock allocated to an order ─────────────
    // Reserved for a specific order
    // Cannot be used for other orders
    ALLOCATED,

    // ── Stock is damaged ────────────────────────
    // Cannot be sold or shipped
    // Needs disposal or return to supplier
    DAMAGED
}