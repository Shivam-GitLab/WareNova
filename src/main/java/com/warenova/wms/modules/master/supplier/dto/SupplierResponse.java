package com.warenova.wms.modules.master.supplier.dto;

import com.warenova.wms.common.enums.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// SUPPLIER RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {

    private Long id;
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String gstNumber;
    private String panNumber;
    private Integer paymentTermsDays;
    private Integer leadTimeDays;
    private String warehouseCode;
    private SupplierStatus status;

    // ── Audit fields ──────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}