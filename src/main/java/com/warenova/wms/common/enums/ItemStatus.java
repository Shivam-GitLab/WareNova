package com.warenova.wms.common.enums;

// ================================================
// ITEM STATUS ENUM
// ================================================
// Defines the lifecycle status of an item/SKU
// Used in:
//   Item entity → status field
//   Item search → filter by status
//   Receiving → only ACTIVE items allowed
// ================================================
public enum ItemStatus {

    // ── Item is active ──────────────────────────
    // Can be received, stored, picked, shipped
    // Default status for new items
    ACTIVE,

    // ── Item is inactive ────────────────────────
    // Temporarily disabled
    // Existing stock can be shipped
    // No new receiving allowed
    INACTIVE,

    // ── Item is discontinued ────────────────────
    // Product line ended
    // Only remaining stock can be cleared
    // No new POs allowed
    DISCONTINUED
}