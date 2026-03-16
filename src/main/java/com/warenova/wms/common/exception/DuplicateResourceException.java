package com.warenova.wms.common.exception;

// ================================================
// DUPLICATE RESOURCE EXCEPTION
// ================================================
// PURPOSE:
// Thrown when trying to create a record
// that already exists
//
// Maps to HTTP 409 Conflict
//
// USAGE EXAMPLES:
// throw new DuplicateResourceException(
//   "Item", "SKU", "SKU001");
//
// throw new DuplicateResourceException(
//   "User", "username", "john.doe");
// ================================================
public class DuplicateResourceException
        extends WMSException {

    // ── Constructor ──────────────────────────────
    // resourceName = "Item", "User"
    // fieldName    = "SKU", "username", "email"
    // fieldValue   = "SKU001", "john.doe"
    public DuplicateResourceException(
            String resourceName,
            String fieldName,
            String fieldValue) {
        // HTTP 409 = Conflict
        super(resourceName + " already exists with "
                + fieldName + ": " + fieldValue, 409);
    }
}