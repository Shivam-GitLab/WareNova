package com.warenova.wms.modules.master.carrier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// CARRIER REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrierRequest {

    @NotBlank(message = "Carrier code is required")
    private String carrierCode;

    @NotBlank(message = "Carrier name is required")
    private String carrierName;

    private String carrierType;
    private String contactPerson;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private String contactPhone;
    private String trackingUrl;
    private String accountNumber;
    private String serviceTypes;
    private Double maxWeightKg;

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;
}