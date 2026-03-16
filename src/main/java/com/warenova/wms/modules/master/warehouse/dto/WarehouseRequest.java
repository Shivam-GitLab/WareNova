package com.warenova.wms.modules.master.warehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// WAREHOUSE REQUEST DTO
// ================================================
// Used for both CREATE and UPDATE operations
// Received FROM client
// Validated before hitting service
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Warehouse name is required")
    private String warehouseName;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String contactPerson;
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private Double totalAreaSqft;

    @NotNull(message = "3PL flag is required")
    private Boolean is3pl;
}