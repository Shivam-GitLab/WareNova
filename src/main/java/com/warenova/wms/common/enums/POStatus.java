package com.warenova.wms.common.enums;

// ================================================
// PURCHASE ORDER STATUS ENUM
// ================================================
// Tracks the lifecycle of a Purchase Order
// in the inbound process
//
// LIFECYCLE FLOW:
// OPEN → PARTIALLY_RECEIVED → FULLY_RECEIVED
//                                   ↓
//                                CLOSED
// OR
// OPEN → CANCELLED
// ================================================
public enum POStatus {

    // ── PO created, receiving not started ───────
    // Waiting for goods to arrive
    OPEN,

    // ── Some items received ─────────────────────
    // Partial delivery received
    // More items still expected
    PARTIALLY_RECEIVED,

    // ── All items received ──────────────────────
    // All ordered qty received
    // Ready to be closed
    FULLY_RECEIVED,

    // ── PO is closed ────────────────────────────
    // All processing complete
    // No more receiving allowed
    CLOSED,

    // ── PO is cancelled ─────────────────────────
    // Order cancelled before receiving
    // Or after partial receiving
    CANCELLED
}