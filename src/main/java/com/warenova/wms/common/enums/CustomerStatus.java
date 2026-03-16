package com.warenova.wms.common.enums;

// ================================================
// CUSTOMER STATUS ENUM
// ================================================
// Defines the status of a customer
// Used in:
//   Customer entity → status field
//   Order creation → only ACTIVE customers
// ================================================
public enum CustomerStatus {

    // ── Customer is active ──────────────────────
    // Orders can be placed and shipped
    ACTIVE,

    // ── Customer is inactive ────────────────────
    // Temporarily disabled
    // No new orders allowed
    INACTIVE,

    // ── Customer is suspended ───────────────────
    // Due to payment issues or disputes
    // Existing orders on hold
    SUSPENDED
}