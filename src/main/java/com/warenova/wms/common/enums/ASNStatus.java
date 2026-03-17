package com.warenova.wms.common.enums;

public enum ASNStatus {

    CREATED,      // ASN created by supplier
    IN_TRANSIT,   // Truck on the way
    ARRIVED,      // Truck at dock
    RECEIVING,    // ← THIS WAS MISSING!
    CLOSED,       // Receiving complete
    CANCELLED     // ← THIS WAS MISSING!
}