package com.warenova.wms.modules.master.carrier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// CARRIER RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrierResponse {

    private Long id;
    private String carrierCode;
    private String carrierName;
    private String carrierType;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private String trackingUrl;
    private String accountNumber;
    private String serviceTypes;
    private Double maxWeightKg;
    private String warehouseCode;
    private Boolean active;

    // ── Audit fields ──────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}