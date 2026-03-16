package com.warenova.wms.common.enums;

// ================================================
// SHIPMENT STATUS ENUM
// ================================================
// Tracks the shipment from warehouse to customer
//
// LIFECYCLE:
// CREATED → MANIFESTED → DISPATCHED
//               ↓
//           IN_TRANSIT → DELIVERED
// ================================================
public enum ShipmentStatus {

    // ── Shipment record created ─────────────────
    CREATED,

    // ── Shipment added to carrier manifest ──────
    // BOL generated, carrier assigned
    MANIFESTED,

    // ── Truck left warehouse ────────────────────
    DISPATCHED,

    // ── Shipment on the way ─────────────────────
    IN_TRANSIT,

    // ── Delivered to customer ───────────────────
    DELIVERED,

    // ── Delivery failed ─────────────────────────
    // Wrong address or customer not available
    DELIVERY_FAILED,

    // ── Shipment returned ───────────────────────
    // Returned to warehouse
    RETURNED
}