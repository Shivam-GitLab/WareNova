package com.warenova.wms.modules.inbound.crossdock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrossDockRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    // ── Inbound ───────────────────────────────────
    private String asnNumber;
    private String poNumber;

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    private String inboundLpn;

    @NotBlank(message = "Receiving location required")
    private String receivingLocation;

    // ── Item ─────────────────────────────────────
    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    // ── Outbound ──────────────────────────────────
    @NotBlank(message = "Sales order is required")
    private String salesOrderNumber;

    @NotBlank(message = "Customer code is required")
    private String customerCode;

    @NotBlank(message = "Dispatch location required")
    private String dispatchLocation;

    private String outboundLpn;
    private String carrierCode;
}