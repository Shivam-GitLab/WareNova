package com.warenova.wms.modules.inbound.grn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrnRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "ASN number is required")
    private String asnNumber;

    private String poNumber;

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotNull(message = "Received date is required")
    private LocalDateTime receivedDate;

    @NotNull(message = "Expected quantity required")
    @Positive(message = "Must be positive")
    private Double expectedQuantity;

    @NotNull(message = "Received quantity required")
    @Positive(message = "Must be positive")
    private Double receivedQuantity;

    private Double damagedQuantity;
    private Double rejectedQuantity;
    private String appointmentNumber;
    private String dockDoor;
    private String vehicleNumber;
    private String notes;
}