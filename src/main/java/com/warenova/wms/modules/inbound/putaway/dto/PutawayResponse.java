package com.warenova.wms.modules.inbound.putaway.dto;

import com.warenova.wms.common.enums.PutawayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// PUTAWAY RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PutawayResponse {

    private Long id;
    private String putawayNumber;
    private String warehouseCode;
    private String receivingNumber;
    private String grnNumber;
    private String lpnNumber;
    private String sku;
    private Double quantity;
    private String fromLocation;
    private String suggestedLocation;
    private String toLocation;
    private String putawayType;
    private String assignedTo;
    private String notes;
    private PutawayStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}