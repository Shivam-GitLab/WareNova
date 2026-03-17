package com.warenova.wms.modules.inbound.putaway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// PUTAWAY REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutawayRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(
            message = "Receiving number is required")
    private String receivingNumber;

    private String grnNumber;

    @NotBlank(message = "LPN number is required")
    private String lpnNumber;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Must be positive")
    private Double quantity;

    @NotBlank(
            message = "From location is required")
    private String fromLocation;

    private String suggestedLocation;
    private String toLocation;
    private String putawayType;
    private String assignedTo;
    private String notes;
}