package com.warenova.wms.modules.master.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// SUPPLIER REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    private String contactPerson;

    @Email(message = "Invalid email format")
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

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;
}