package com.warenova.wms.modules.master.warehouse.dto;

import com.warenova.wms.common.enums.WarehouseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// WAREHOUSE RESPONSE DTO
// ================================================
// Sent TO client
// Never expose Entity directly!
// Select only fields client needs
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {

    private Long id;
    private String warehouseCode;
    private String warehouseName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private Double totalAreaSqft;
    private WarehouseStatus status;
    private Boolean is3pl;

    // ── Audit fields from BaseEntity ─────────────
    // Show who created and when
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}