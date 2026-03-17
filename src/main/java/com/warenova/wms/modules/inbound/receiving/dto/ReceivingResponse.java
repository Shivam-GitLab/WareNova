package com.warenova.wms.modules.inbound.receiving.dto;

import com.warenova.wms.common.enums.ReceivingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceivingResponse {

    private Long id;
    private String receivingNumber;
    private String warehouseCode;
    private String asnNumber;
    private String poNumber;
    private String supplierCode;
    private String sku;
    private Double expectedQuantity;
    private Double receivedQuantity;
    private Double acceptedQuantity;
    private Double damagedQuantity;
    private Double rejectedQuantity;
    private Double shortageQuantity;
    private String dockDoor;
    private String stagingLocation;
    private String lpnNumber;
    private String vehicleNumber;
    private LocalDateTime receivedDateTime;
    private String igpNumber;
    private String notes;
    private ReceivingStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}