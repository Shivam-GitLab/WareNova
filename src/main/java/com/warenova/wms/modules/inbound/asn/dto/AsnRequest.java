package com.warenova.wms.modules.inbound.asn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsnRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotBlank(message = "PO number is required")
    private String poNumber;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "ASN quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double asnQuantity;

    private String vehicleNumber;
    private String driverName;
    private String driverPhone;

    @NotNull(message = "Expected date is required")
    private LocalDate expectedDate;

    private String appointmentNumber;
    private String dockDoor;
    private Integer palletCount;
    private Integer cartonCount;
    private String notes;
}