package com.warenova.wms.modules.master.warehouse.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.WarehouseStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// WAREHOUSE ENTITY
// ================================================
// PURPOSE:
// Represents a physical warehouse facility
// This is the TOP LEVEL master data
// ALL other data belongs to a warehouse
//
// REAL WORLD EXAMPLE (from Trangile/Infor WMS):
// WH001 = Mumbai Warehouse
// WH002 = Delhi Warehouse
// WH003 = Bangalore Warehouse
//
// Every Location, Item, Order belongs
// to a specific warehouse
//
// TABLE: wms_warehouse
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_warehouse",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_warehouse_code",
                        columnNames = "warehouse_code"
                )
        }
)
public class Warehouse extends BaseEntity {

    // ================================================
    // PRIMARY KEY
    // ================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // WAREHOUSE CODE
    // ================================================
    // Unique short code for warehouse
    // Example: WH001, WH002, MUM-WH-01
    // Used in all transactions as reference
    // Cannot be changed after creation
    // ================================================
    @Column(
            name = "warehouse_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String warehouseCode;

    // ================================================
    // WAREHOUSE NAME
    // ================================================
    // Full name of warehouse
    // Example: "Mumbai Central Warehouse"
    // ================================================
    @Column(
            name = "warehouse_name",
            nullable = false,
            length = 100
    )
    private String warehouseName;

    // ================================================
    // ADDRESS FIELDS
    // ================================================
    @Column(name = "address_line1", length = 200)
    private String addressLine1;

    @Column(name = "address_line2", length = 200)
    private String addressLine2;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "pincode", length = 10)
    private String pincode;

    // ================================================
    // CONTACT DETAILS
    // ================================================
    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "contact_phone", length = 15)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    // ================================================
    // WAREHOUSE CAPACITY
    // ================================================
    // Total storage capacity in square feet
    // Used for capacity planning reports
    // ================================================
    @Column(name = "total_area_sqft")
    private Double totalAreaSqft;

    // ================================================
    // STATUS
    // ================================================
    // ACTIVE / INACTIVE / UNDER_MAINTENANCE
    // Only ACTIVE warehouses can process orders
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private WarehouseStatus status = WarehouseStatus.ACTIVE;

    // ================================================
    // 3PL FLAG
    // ================================================
    // Is this a 3PL (Third Party Logistics) warehouse?
    // true  = manages multiple client inventories
    // false = single company warehouse
    // From Trangile experience with Infor WMS!
    // ================================================
    @Column(name = "is_3pl")
    @Builder.Default
    private Boolean is3pl = false;
}