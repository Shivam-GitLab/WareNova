package com.warenova.wms.common.enums;

public enum POStatus {
    DRAFT,        // Being created
    CONFIRMED,    // Sent to supplier
    PARTIALLY_RECEIVED, // Some items received
    FULLY_RECEIVED,     // All items received
    CLOSED,       // Completed
    CANCELLED     // Cancelled
}