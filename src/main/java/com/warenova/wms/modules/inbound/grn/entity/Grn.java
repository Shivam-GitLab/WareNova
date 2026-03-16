package com.warenova.wms.modules.inbound.grn.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.GrnStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// ================================================
// GRN ENTITY — Goods Receipt Note
// ================================================
// PURPOSE:
// Legal document confirming goods received
// Generated AFTER ASN is closed
// Triggers inventory update in system
//
// REAL WORLD FLOW:
// ASN arrives → Pre-Receiving → Receiving
// → ASN Closed → GRN Generated ✅
// → Inventory Updated → Putaway Ready
//
// USED IN:
// → Accounts Payable (payment to supplier)
// → Inventory updates
// → Audit trail
//
// TABLE: wms_grn
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_grn",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_grn_number",
                        columnNames = "grn_number"
                )
        }
)
public class Grn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // GRN NUMBER
    // ================================================
    // Auto generated unique number
    // Example: GRN-20240115-001
    // ================================================
    @Column(
            name = "grn_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String grnNumber;

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
    // GRN is always against an ASN
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
    @Column(
            name = "po_number",
            length = 30
    )
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
    // RECEIVED DATE
    // ================================================
    @Column(
            name = "received_date",
            nullable = false
    )
    private LocalDateTime receivedDate;

    // ================================================
    // QUANTITIES
    // ================================================
    // Expected vs Actually received
    // Variance triggers quality check
    // ================================================
    @Column(name = "expected_quantity")
    private Double expectedQuantity;

    @Column(name = "received_quantity")
    private Double receivedQuantity;

    @Column(name = "damaged_quantity")
    private Double damagedQuantity;

    @Column(name = "rejected_quantity")
    private Double rejectedQuantity;

    // ================================================
    // ACCEPTED QUANTITY
    // ================================================
    // received - damaged - rejected
    // Goes to inventory
    // ================================================
    @Column(name = "accepted_quantity")
    private Double acceptedQuantity;

    // ================================================
    // APPOINTMENT NUMBER
    // ================================================
    @Column(name = "appointment_number", length = 30)
    private String appointmentNumber;

    // ================================================
    // DOCK DOOR
    // ================================================
    @Column(name = "dock_door", length = 20)
    private String dockDoor;

    // ================================================
    // VEHICLE NUMBER
    // ================================================
    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

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
    private GrnStatus status = GrnStatus.DRAFT;
}