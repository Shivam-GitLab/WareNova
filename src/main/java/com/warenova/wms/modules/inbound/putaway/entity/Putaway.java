package com.warenova.wms.modules.inbound.putaway.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.PutawayStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// PUTAWAY ENTITY
// ================================================
// PURPOSE:
// Move goods from STAGING/DOCK location
// to actual BIN storage location
//
// REAL WORLD FLOW:
// Receiving done → Goods at STAGE-01
// → Putaway task created
// → Staff scans LPN at STAGE-01
// → System suggests A-01-01-01
// → Staff moves LPN to A-01-01-01
// → Scans location to confirm
// → Inventory updated
//
// TWO TYPES (from PPT):
// 1. INLOC MOVE   → Stage to GPD
// 2. ASSISTED     → Stage/GPD to Bin Location
//
// TABLE: wms_putaway
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_putaway",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_putaway_number",
                        columnNames = "putaway_number"
                )
        }
)
public class Putaway extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // PUTAWAY NUMBER
    // ================================================
    // Auto generated
    // Example: PUT-20240122001
    // ================================================
    @Column(
            name = "putaway_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String putawayNumber;

    // ================================================
    // WAREHOUSE CODE
    // ================================================
    @Column(
            name = "warehouse_code",
            nullable = false,
            length = 20
    )
    private String warehouseCode;

    // ================================================
    // RECEIVING NUMBER
    // ================================================
    // Which receiving this putaway is for
    // ================================================
    @Column(
            name = "receiving_number",
            nullable = false,
            length = 30
    )
    private String receivingNumber;

    // ================================================
    // GRN NUMBER
    // ================================================
    @Column(name = "grn_number", length = 30)
    private String grnNumber;

    // ================================================
    // LPN NUMBER
    // ================================================
    // Which LPN (pallet/carton) being put away
    // ================================================
    @Column(
            name = "lpn_number",
            nullable = false,
            length = 30
    )
    private String lpnNumber;

    // ================================================
    // SKU
    // ================================================
    @Column(
            name = "sku",
            nullable = false,
            length = 50
    )
    private String sku;

    // ================================================
    // QUANTITY
    // ================================================
    @Column(
            name = "quantity",
            nullable = false
    )
    private Double quantity;

    // ================================================
    // FROM LOCATION
    // ================================================
    // Where goods currently are
    // Example: STAGE-01, DOCK-01
    // ================================================
    @Column(
            name = "from_location",
            nullable = false,
            length = 30
    )
    private String fromLocation;

    // ================================================
    // SUGGESTED LOCATION
    // ================================================
    // System suggested best location
    // Based on item type, zone, availability
    // ================================================
    @Column(
            name = "suggested_location",
            length = 30
    )
    private String suggestedLocation;

    // ================================================
    // TO LOCATION
    // ================================================
    // Actual location where goods placed
    // Staff can override suggested location
    // ================================================
    @Column(name = "to_location", length = 30)
    private String toLocation;

    // ================================================
    // PUTAWAY TYPE
    // ================================================
    // INLOC_MOVE   → Stage to GPD
    // ASSISTED     → GPD/Stage to Bin
    // ================================================
    @Column(name = "putaway_type", length = 20)
    @Builder.Default
    private String putawayType = "ASSISTED";

    // ================================================
    // ASSIGNED TO
    // ================================================
    // Which staff member assigned this task
    // ================================================
    @Column(name = "assigned_to", length = 50)
    private String assignedTo;

    // ================================================
    // NOTES
    // ================================================
    @Column(name = "notes", length = 500)
    private String notes;

    // ================================================
    // STATUS
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private PutawayStatus status =
            PutawayStatus.PENDING;
}