package com.warenova.wms.modules.master.customer.dto;

import com.warenova.wms.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// CUSTOMER RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String customerCode;
    private String customerName;
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
    private Double creditLimit;
    private Integer paymentTermsDays;
    private String warehouseCode;
    private CustomerStatus status;

    // ── Audit fields ──────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}