package com.warenova.wms.common.enums;

public enum LpnStatus {
    EMPTY,      // No inventory assigned
    ACTIVE,     // Has inventory, in warehouse
    IN_TRANSIT, // Moving between locations
    CLOSED,     // Putaway complete
    SHIPPED,    // Left warehouse
    CANCELLED   // Voided
}