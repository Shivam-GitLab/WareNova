package com.warenova.wms.modules.master.item.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// ITEM ENTITY
// ================================================
// PURPOSE:
// Represents a product/SKU stored in warehouse
// Called "Item Master" in WMS terminology
//
// EXTENDS BaseEntity:
// Auto gets created_at, updated_at,
// created_by, updated_by columns
//
// REAL WORLD EXAMPLE (from Infor WMS):
// SKU001 = iPhone 15 Pro 256GB Black
// SKU002 = Samsung TV 55inch
// SKU003 = Nike Shoes Size 42
//
// Item master is created ONCE
// Then used in all inbound/outbound operations
//
// TABLE: wms_item
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_item_sku",
                        columnNames = "sku"
                )
        }
)
public class Item extends BaseEntity {

    // ================================================
    // PRIMARY KEY
    // ================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // SKU (Stock Keeping Unit)
    // ================================================
    // Unique product identifier
    // Example: SKU001, ITEM-001, APPLE-IP15-256-BLK
    // Used in all transactions
    // Cannot be changed after creation
    // ================================================
    @Column(
            name = "sku",
            nullable = false,
            unique = true,
            length = 50
    )
    private String sku;

    // ================================================
    // ITEM NAME
    // ================================================
    // Full product name
    // Example: "iPhone 15 Pro 256GB Black"
    // ================================================
    @Column(
            name = "item_name",
            nullable = false,
            length = 200
    )
    private String itemName;

    // ================================================
    // DESCRIPTION
    // ================================================
    // Detailed product description
    // ================================================
    @Column(
            name = "description",
            length = 500
    )
    private String description;

    // ================================================
    // CATEGORY
    // ================================================
    // Product category
    // Example: Electronics, Apparel, FMCG
    // ================================================
    @Column(name = "category", length = 50)
    private String category;

    // ================================================
    // SUB CATEGORY
    // ================================================
    // Product sub category
    // Example: Mobile Phones, Laptops
    // ================================================
    @Column(name = "sub_category", length = 50)
    private String subCategory;

    // ================================================
    // BRAND
    // ================================================
    @Column(name = "brand", length = 50)
    private String brand;

    // ================================================
    // UOM (Unit of Measure)
    // ================================================
    // How item is measured/counted
    // Example: EA (Each), KG, LTR, BOX, PALLET
    // ================================================
    @Column(
            name = "uom",
            nullable = false,
            length = 10
    )
    private String uom;

    // ================================================
    // DIMENSIONS
    // ================================================
    // Physical dimensions for space planning
    // Used to calculate storage capacity needed
    // ================================================
    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "length_cm")
    private Double lengthCm;

    @Column(name = "width_cm")
    private Double widthCm;

    @Column(name = "height_cm")
    private Double heightCm;

    // ================================================
    // WAREHOUSE CODE
    // ================================================
    // Which warehouse this item belongs to
    // ================================================
    @Column(
            name = "warehouse_code",
            nullable = false,
            length = 20
    )
    private String warehouseCode;

    // ================================================
    // LOT TRACKING
    // ================================================
    // true  = track by lot/batch number
    // false = no lot tracking
    // Used for pharma, food items
    // ================================================
    @Column(name = "lot_tracking")
    @Builder.Default
    private Boolean lotTracking = false;

    // ================================================
    // SERIAL TRACKING
    // ================================================
    // true  = track each unit by serial number
    // false = no serial tracking
    // Used for electronics, high value items
    // ================================================
    @Column(name = "serial_tracking")
    @Builder.Default
    private Boolean serialTracking = false;

    // ================================================
    // EXPIRY TRACKING
    // ================================================
    // true  = track expiry dates
    // false = no expiry tracking
    // Used for food, pharma items
    // ================================================
    @Column(name = "expiry_tracking")
    @Builder.Default
    private Boolean expiryTracking = false;

    // ================================================
    // REORDER LEVEL
    // ================================================
    // When stock falls below this → alert!
    // Used for inventory replenishment reports
    // ================================================
    @Column(name = "reorder_level")
    private Double reorderLevel;

    // ================================================
    // MIN/MAX STOCK
    // ================================================
    @Column(name = "min_stock_level")
    private Double minStockLevel;

    @Column(name = "max_stock_level")
    private Double maxStockLevel;

    // ================================================
    // PRICE
    // ================================================
    // Used for billing calculations in 3PL
    // ================================================
    @Column(name = "unit_price")
    private Double unitPrice;

    // ================================================
    // STATUS
    // ================================================
    // ACTIVE / INACTIVE / DISCONTINUED
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private ItemStatus status = ItemStatus.ACTIVE;
}