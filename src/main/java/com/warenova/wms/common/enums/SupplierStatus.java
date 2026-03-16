package com.warenova.wms.common.enums;

// ================================================
// SUPPLIER STATUS ENUM
// ================================================
// Defines the status of a supplier/vendor
// Used in:
//   Supplier entity → status field
//   PO creation → only ACTIVE suppliers allowed
// ================================================
public enum SupplierStatus {

    // ── Supplier is active ──────────────────────
    // POs can be raised
    // Goods can be received
    ACTIVE,

    // ── Supplier is inactive ────────────────────
    // Temporarily disabled
    // No new POs allowed
    INACTIVE,

    // ── Supplier is blacklisted ─────────────────
    // Due to fraud or quality issues
    // No transactions allowed at all
    BLACKLISTED
}