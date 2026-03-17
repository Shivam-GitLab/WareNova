package com.warenova.wms.modules.inbound.receiving.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.ReceivingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// ================================================
// RECEIVING ENTITY
// ================================================
// PURPOSE:
// Records actual physical receipt of goods
// at warehouse dock
//
// REAL WORLD FLOW:
// Truck arrives → Staff scans each carton
// → Checks SKU, quantity, damage
// → Records in system
// → Goods moved to STAGING location
// → ASN closed → GRN generated
//
// LINKED TO:
// → ASN (which shipment being received)
// → PO  (which order being fulfilled)
// → GRN (generated after receiving done)
// → LPN (assigned during receiving)
//
// TABLE: wms_receiving
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_receiving",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_receiving_number",
                        columnNames = "receiving_number"
                )
        }
)
public class Receiving extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // RECEIVING NUMBER
    // ================================================
    // Auto generated
    // Example: RCV-20240122001
    // ================================================
    @Column(
            name = "receiving_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String receivingNumber;

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
    // ASN NUMBER
    // ================================================
    @Column(
            name = "asn_number",
            nullable = false,
            length = 30
    )
    private String asnNumber;

    // ================================================
    // PO NUMBER
    // ================================================
    @Column(name = "po_number", length = 30)
    private String poNumber;

    // ================================================
    // SUPPLIER CODE
    // ================================================
    @Column(
            name = "supplier_code",
            nullable = false,
            length = 20
    )
    private String supplierCode;

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
    // QUANTITIES
    // ================================================
    @Column(
            name = "expected_quantity",
            nullable = false
    )
    private Double expectedQuantity;

    @Column(
            name = "received_quantity",
            nullable = false
    )
    private Double receivedQuantity;

    @Column(name = "accepted_quantity")
    private Double acceptedQuantity;

    @Column(name = "damaged_quantity")
    @Builder.Default
    private Double damagedQuantity = 0.0;

    @Column(name = "rejected_quantity")
    @Builder.Default
    private Double rejectedQuantity = 0.0;

    @Column(name = "shortage_quantity")
    private Double shortageQuantity;

    // ================================================
    // DOCK DOOR
    // ================================================
    @Column(name = "dock_door", length = 20)
    private String dockDoor;

    // ================================================
    // STAGING LOCATION
    // ================================================
    // Where goods placed after receiving
    // Before putaway
    // ================================================
    @Column(
            name = "staging_location",
            length = 30
    )
    private String stagingLocation;

    // ================================================
    // LPN NUMBER
    // ================================================
    // Assigned during receiving
    // ================================================
    @Column(name = "lpn_number", length = 30)
    private String lpnNumber;

    // ================================================
    // VEHICLE DETAILS
    // ================================================
    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    // ================================================
    // RECEIVED DATE TIME
    // ================================================
    @Column(
            name = "received_date_time",
            nullable = false
    )
    private LocalDateTime receivedDateTime;

    // ================================================
    // IGP NUMBER
    // ================================================
    // Inbound Gate Pass
    // Generated after pre-receiving
    // ================================================
    @Column(name = "igp_number", length = 30)
    private String igpNumber;

    // ================================================
    // NOTES / REMARKS
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
    private ReceivingStatus status =
            ReceivingStatus.PENDING;
}