package com.warenova.wms.common.enums;

// ================================================
// LOCATION TYPE ENUM
// ================================================
// Defines the purpose/type of a warehouse location
// Used in:
//   Location entity → locationType field
//   Putaway rules → only put in STORAGE
//   Receiving → use DOCK locations
//   Packing → use PACKING locations
// ================================================
public enum LocationType {

    // ── Regular storage location ────────────────
    // Normal racking / shelving
    // Where inventory is stored
    STORAGE,

    // ── Temporary staging area ──────────────────
    // Between receiving and putaway
    // Between picking and packing
    STAGING,

    // ── Receiving / dispatch dock ───────────────
    // Where trucks arrive / depart
    // Inbound and outbound docks
    DOCK,

    // ── Packing station ─────────────────────────
    // Where orders are packed
    // Pack tables and workstations
    PACKING,

    // ── Dispatch / shipping area ────────────────
    // Where packed orders wait for carrier pickup
    DISPATCH,

    // ── Quarantine / hold area ──────────────────
    // Damaged or suspicious goods
    // Quality hold stock
    QUARANTINE
}