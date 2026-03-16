package com.warenova.wms.modules.master.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// ITEM REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Item name is required")
    private String itemName;

    private String description;
    private String category;
    private String subCategory;
    private String brand;

    @NotBlank(message = "UOM is required")
    private String uom;

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    // ── Dimensions ────────────────────────────────
    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @Positive(message = "Length must be positive")
    private Double lengthCm;

    @Positive(message = "Width must be positive")
    private Double widthCm;

    @Positive(message = "Height must be positive")
    private Double heightCm;

    // ── Tracking flags ────────────────────────────
    @NotNull(message = "Lot tracking flag required")
    private Boolean lotTracking;

    @NotNull(message = "Serial tracking flag required")
    private Boolean serialTracking;

    @NotNull(message = "Expiry tracking flag required")
    private Boolean expiryTracking;

    // ── Stock levels ──────────────────────────────
    private Double reorderLevel;
    private Double minStockLevel;
    private Double maxStockLevel;

    // ── Price ─────────────────────────────────────
    @Positive(message = "Unit price must be positive")
    private Double unitPrice;
}