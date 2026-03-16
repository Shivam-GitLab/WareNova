package com.warenova.wms.common.enums;

public enum CrossDockStatus {
    PENDING,      // Waiting for inbound
    IN_PROGRESS,  // Being processed
    COMPLETED,    // Moved to outbound
    CANCELLED     // Cancelled
}