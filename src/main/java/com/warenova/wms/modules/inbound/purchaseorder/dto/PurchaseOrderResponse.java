package com.warenova.wms.modules.inbound.purchaseorder.dto;

import com.warenova.wms.common.enums.POStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {

    private Long id;
    private String poNumber;
    private String warehouseCode;
    private String supplierCode;
    private String sku;
    private Double orderedQuantity;
    private Double receivedQuantity;
    private Double pendingQuantity;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String notes;
    private POStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}