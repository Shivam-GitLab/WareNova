package com.warenova.wms.modules.master.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// CUSTOMER REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Customer code is required")
    private String customerCode;

    @NotBlank(message = "Customer name is required")
    private String customerName;

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
    private Double creditLimit;
    private Integer paymentTermsDays;

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;
}