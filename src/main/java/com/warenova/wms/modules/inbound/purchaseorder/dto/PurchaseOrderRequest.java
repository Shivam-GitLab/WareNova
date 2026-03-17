package com.warenova.wms.modules.inbound.purchaseorder.dto;

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
public class PurchaseOrderRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Ordered quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double orderedQuantity;

    @Positive(message = "Unit price must be positive")
    private Double unitPrice;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private LocalDate expectedDeliveryDate;
    private String notes;
}