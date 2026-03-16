package com.warenova.wms.modules.master.item.dto;

import com.warenova.wms.common.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// ITEM RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String sku;
    private String itemName;
    private String description;
    private String category;
    private String subCategory;
    private String brand;
    private String uom;
    private String warehouseCode;
    private Double weightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Boolean lotTracking;
    private Boolean serialTracking;
    private Boolean expiryTracking;
    private Double reorderLevel;
    private Double minStockLevel;
    private Double maxStockLevel;
    private Double unitPrice;
    private ItemStatus status;

    // ── Audit fields ──────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}