package com.warenova.wms.common.enums;

// ================================================
// PICK STATUS ENUM
// ================================================
// Tracks individual pick task status
// One pick task = pick one item from one location
//
// LIFECYCLE:
// PENDING → ASSIGNED → IN_PROGRESS → COMPLETED
// ================================================
public enum PickStatus {

    // ── Task created, not assigned ──────────────
    PENDING,

    // ── Task assigned to a picker ───────────────
    ASSIGNED,

    // ── Picker is working on this task ──────────
    IN_PROGRESS,

    // ── Pick confirmed by picker ─────────────────
    COMPLETED,

    // ── Item not found at location ──────────────
    SHORT_PICKED,

    // ── Task cancelled ──────────────────────────
    CANCELLED
}