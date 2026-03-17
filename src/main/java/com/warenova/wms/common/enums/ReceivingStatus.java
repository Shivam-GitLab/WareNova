package com.warenova.wms.common.enums;

public enum ReceivingStatus {
    PENDING,      // Waiting to be received
    IN_PROGRESS,  // Being received
    COMPLETED,    // Receiving done
    CANCELLED     // Cancelled
}