package com.warenova.wms.common.constants;

// ================================================
// WMS CONSTANTS
// ================================================
// PURPOSE:
// Stores all constant values used across
// the entire WareNova WMS application
//
// WHY USE CONSTANTS?
// ❌ BAD:  if (status.equals("ACTIVE")) { }
// ✅ GOOD: if (status.equals(WMSConstants.ACTIVE))
//
// Benefits:
// → No magic strings scattered in code
// → Change value in ONE place
// → Less chance of typos
// → Easy to find all usages
// ================================================
public final class WMSConstants {

    // ================================================
    // PRIVATE CONSTRUCTOR
    // Prevents instantiation of this class
    // This is a utility class — no objects needed
    // ================================================
    private WMSConstants() {
        throw new UnsupportedOperationException(
                "WMSConstants is a utility class"
        );
    }

    // ================================================
    // JWT CONSTANTS
    // ================================================
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_ROLE_KEY = "role";

    // ================================================
    // API RESPONSE MESSAGES
    // ================================================
    public static final String SUCCESS = "Success";
    public static final String CREATED = "Created successfully";
    public static final String UPDATED = "Updated successfully";
    public static final String DELETED = "Deleted successfully";
    public static final String NOT_FOUND = "Record not found";
    public static final String ALREADY_EXISTS = "Record already exists";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String UNAUTHORIZED = "Unauthorized access";

    // ================================================
    // WMS BUSINESS CONSTANTS
    // ================================================

    // Over receipt tolerance = 10%
    // If PO qty = 100, max receive = 110
    public static final double OVER_RECEIPT_TOLERANCE = 0.10;

    // Minimum stock alert threshold
    public static final int LOW_STOCK_THRESHOLD = 10;

    // Default page size for all list APIs
    public static final int DEFAULT_PAGE_SIZE = 20;

    // Maximum page size allowed
    public static final int MAX_PAGE_SIZE = 100;

    // ================================================
    // NUMBER GENERATION PREFIXES
    // ================================================
    // Used to generate unique transaction numbers

    // PO number prefix → PO-2024-000001
    public static final String PO_PREFIX = "PO-";

    // ASN number prefix → ASN-2024-000001
    public static final String ASN_PREFIX = "ASN-";

    // Wave number prefix → WAVE-000001
    public static final String WAVE_PREFIX = "WAVE-";

    // LPN number prefix → LPN-000001
    // LPN = License Plate Number (pallet/carton ID)
    public static final String LPN_PREFIX = "LPN-";

    // Sales order prefix → SO-2024-000001
    public static final String SO_PREFIX = "SO-";

    // Shipment prefix → SHP-2024-000001
    public static final String SHP_PREFIX = "SHP-";

    // ================================================
    // WAREHOUSE OPERATION CONSTANTS
    // ================================================

    // Default system user when no one is logged in
    public static final String SYSTEM_USER = "SYSTEM";

    // Default warehouse code
    public static final String DEFAULT_WAREHOUSE = "WH001";
}