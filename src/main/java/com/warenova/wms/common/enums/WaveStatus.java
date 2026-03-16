package com.warenova.wms.common.enums;

// ================================================
// WAVE STATUS ENUM
// ================================================
// Wave = group of orders picked together
// for efficiency
//
// LIFECYCLE:
// PLANNED → RELEASED → PICKING → COMPLETE
// ================================================
public enum WaveStatus {

    // ── Wave created but not released ───────────
    // Orders grouped, tasks not yet generated
    PLANNED,

    // ── Wave released ───────────────────────────
    // Pick tasks generated for pickers
    RELEASED,

    // ── Picking in progress ─────────────────────
    // At least one task being worked on
    PICKING,

    // ── All tasks complete ──────────────────────
    // All orders in wave fully picked
    COMPLETE,

    // ── Wave cancelled ──────────────────────────
    CANCELLED
}