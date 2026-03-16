package com.warenova.wms.common.enums;

// ================================================
// ASN STATUS ENUM
// ================================================
// ASN = Advanced Shipment Notice
// Sent by supplier before goods arrive
//
// LIFECYCLE FLOW:
// CREATED → IN_TRANSIT → ARRIVED
//               ↓
//          RECEIVING_IN_PROGRESS
//               ↓
//           RECEIVED → CLOSED
// ================================================
public enum ASNStatus {

    // ── ASN created by supplier ─────────────────
    // Goods not yet shipped
    CREATED,

    // ── Goods are in transit ────────────────────
    // Truck on the way to warehouse
    IN_TRANSIT,

    // ── Truck arrived at warehouse ──────────────
    // Waiting for dock assignment
    ARRIVED,

    // ── Receiving is in progress ────────────────
    // Warehouse team unloading & scanning
    RECEIVING_IN_PROGRESS,

    // ── All goods received ──────────────────────
    // All items scanned and put in system
    RECEIVED,

    // ── ASN is closed ───────────────────────────
    // All processing complete
    CLOSED
}