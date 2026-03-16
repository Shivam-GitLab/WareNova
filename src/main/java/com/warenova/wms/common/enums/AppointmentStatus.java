package com.warenova.wms.common.enums;

public enum AppointmentStatus {
    SCHEDULED,    // Booked in advance
    CONFIRMED,    // Supplier confirmed
    IN_PROGRESS,  // Truck arrived
    COMPLETED,    // Done
    CANCELLED,    // Cancelled
    NO_SHOW       // Supplier didn't arrive
}