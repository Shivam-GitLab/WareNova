package com.warenova.wms.common.enums;

// ================================================
// ORDER STATUS ENUM
// ================================================
// Tracks lifecycle of a Sales/Outbound Order
//
// LIFECYCLE FLOW:
// NEW → RELEASED → WAVED → PICKING
//                              ↓
//                           PICKED → PACKING
//                              ↓
//                           PACKED → SHIPPED
//                              ↓
//                           DELIVERED
// OR at any stage → CANCELLED
// ================================================
public enum OrderStatus {

    // ── Order just created ──────────────────────
    // Waiting to be processed
    NEW,

    // ── Order released for processing ───────────
    // Approved and ready for waving
    RELEASED,

    // ── Order added to a wave ───────────────────
    // Grouped with other orders for picking
    WAVED,

    // ── Picking in progress ─────────────────────
    // Warehouse picker is collecting items
    PICKING,

    // ── All items picked ────────────────────────
    // Ready to go to pack station
    PICKED,

    // ── Packing in progress ─────────────────────
    // Items being packed in cartons
    PACKING,

    // ── Order fully packed ──────────────────────
    // Ready for shipment
    PACKED,

    // ── Order handed to carrier ─────────────────
    // Carrier has collected the shipment
    SHIPPED,

    // ── Order delivered to customer ─────────────
    // Delivery confirmed
    DELIVERED,

    // ── Order cancelled ─────────────────────────
    // Cancelled before shipping
    CANCELLED
}