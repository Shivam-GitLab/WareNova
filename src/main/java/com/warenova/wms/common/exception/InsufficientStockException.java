package com.warenova.wms.common.exception;

// ================================================
// INSUFFICIENT STOCK EXCEPTION
// ================================================
// PURPOSE:
// Thrown when trying to pick or allocate
// more stock than what is available
//
// Maps to HTTP 400 Bad Request
//
// USAGE EXAMPLE:
// throw new InsufficientStockException(
//   "SKU001", 50.0, 30.0);
// Message: "Insufficient stock for SKU001.
//   Required: 50.0, Available: 30.0"
// ================================================
public class InsufficientStockException
        extends WMSException {

    // ── Constructor ──────────────────────────────
    // itemSku       = "SKU001"
    // requiredQty   = how much is needed
    // availableQty  = how much is in stock
    public InsufficientStockException(
            String itemSku,
            Double requiredQty,
            Double availableQty) {
        super("Insufficient stock for item: " + itemSku
                + ". Required: " + requiredQty
                + ", Available: " + availableQty, 400);
    }
}