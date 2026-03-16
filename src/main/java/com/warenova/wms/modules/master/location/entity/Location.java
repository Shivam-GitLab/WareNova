package com.warenova.wms.modules.master.location.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.enums.LocationType;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// LOCATION ENTITY
// ================================================
// PURPOSE:
// Represents a physical storage location
// inside a warehouse
//
// REAL WORLD EXAMPLE (from Infor WMS):
// Location code: A-01-01-01
// Meaning:
// A    = Zone A
// 01   = Aisle 1
// 01   = Bay 1
// 01   = Level 1
//
// Every item stored in WMS has a location
// Putaway assigns items to locations
// Picking retrieves items from locations
//
// TABLE: wms_location
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_location",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_location_code_warehouse",
                        columnNames = {
                                "location_code",
                                "warehouse_code"
                        }
                )
        }
)
public class Location extends BaseEntity {

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
    // Which warehouse this location belongs to
    // Example: WH001, WH002
    // Not a FK relation — stored as string
    // for performance (no joins needed)
    // ================================================
    @Column(
            name = "warehouse_code",
            nullable = false,
            length = 20
    )
    private String warehouseCode;

    // ================================================
    // LOCATION CODE
    // ================================================
    // Unique code within warehouse
    // Format: ZONE-AISLE-BAY-LEVEL
    // Example: A-01-01-01
    // Scanned by barcode scanner in warehouse
    // ================================================
    @Column(
            name = "location_code",
            nullable = false,
            length = 30
    )
    private String locationCode;

    // ================================================
    // ZONE
    // ================================================
    // Physical zone in warehouse
    // Example: A, B, C or ZONE-A, COLD-ZONE
    // ================================================
    @Column(name = "zone", length = 20)
    private String zone;

    // ================================================
    // AISLE
    // ================================================
    // Aisle number within zone
    // Example: 01, 02, 03
    // ================================================
    @Column(name = "aisle", length = 10)
    private String aisle;

    // ================================================
    // BAY
    // ================================================
    // Bay number within aisle
    // Example: 01, 02, 03
    // ================================================
    @Column(name = "bay", length = 10)
    private String bay;

    // ================================================
    // LEVEL
    // ================================================
    // Vertical level of rack
    // Example: 01=ground, 02=middle, 03=top
    // ================================================
    @Column(name = "level", length = 10)
    private String level;

    // ================================================
    // LOCATION TYPE
    // ================================================
    // STORAGE, STAGING, DOCK, PACKING,
    // DISPATCH, QUARANTINE
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "location_type",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private LocationType locationType =
            LocationType.STORAGE;

    // ================================================
    // LOCATION STATUS
    // ================================================
    // EMPTY, OCCUPIED, FULL, BLOCKED
    // Updated automatically when stock moves
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private LocationStatus status =
            LocationStatus.EMPTY;

    // ================================================
    // CAPACITY
    // ================================================
    // Maximum weight location can hold (kg)
    @Column(name = "max_weight_kg")
    private Double maxWeightKg;

    // Maximum volume location can hold (cubic meters)
    @Column(name = "max_volume_cbm")
    private Double maxVolumeCbm;

    // ================================================
    // MIXED SKU FLAG
    // ================================================
    // true  = multiple SKUs allowed in location
    // false = only one SKU per location
    // In premium WMS = false for accuracy
    // ================================================
    @Column(name = "allow_mixed_sku")
    @Builder.Default
    private Boolean allowMixedSku = false;

    // ================================================
    // ACTIVE FLAG
    // ================================================
    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;
}