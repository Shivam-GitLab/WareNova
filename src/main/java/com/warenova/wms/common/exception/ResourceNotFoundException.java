package com.warenova.wms.common.exception;

// ================================================
// RESOURCE NOT FOUND EXCEPTION
// ================================================
// PURPOSE:
// Thrown when a requested record does not
// exist in the database
//
// Maps to HTTP 404 Not Found
//
// USAGE EXAMPLES:
// throw new ResourceNotFoundException(
//   "Item", "SKU001");
//
// throw new ResourceNotFoundException(
//   "Purchase Order", "PO-2024-001");
// ================================================
public class ResourceNotFoundException
        extends WMSException {

    // ── Constructor ──────────────────────────────
    // resourceName = "Item", "Order", "Location"
    // identifier   = "SKU001", "PO-001", "A-01-01"
    public ResourceNotFoundException(
            String resourceName,
            String identifier) {
        // HTTP 404 = Not Found
        super(resourceName + " not found with id/code: "
                + identifier, 404);
    }
}