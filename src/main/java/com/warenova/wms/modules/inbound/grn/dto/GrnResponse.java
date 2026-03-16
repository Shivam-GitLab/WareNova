package com.warenova.wms.modules.inbound.grn.dto;

import com.warenova.wms.common.enums.GrnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrnResponse {

    private Long id;
    private String grnNumber;
    private String warehouseCode;
    private String asnNumber;
    private String poNumber;
    private String supplierCode;
    private LocalDateTime receivedDate;
    private Double expectedQuantity;
    private Double receivedQuantity;
    private Double damagedQuantity;
    private Double rejectedQuantity;
    private Double acceptedQuantity;
    private String appointmentNumber;
    private String dockDoor;
    private String vehicleNumber;
    private String notes;
    private GrnStatus status;

    // ── Variance info ─────────────────────────────
    private Double varianceQuantity;
    private Double variancePercentage;

    // ── Audit ─────────────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}