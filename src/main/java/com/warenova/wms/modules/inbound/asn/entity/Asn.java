package com.warenova.wms.modules.inbound.asn.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.ASNStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

// ================================================
// ASN ENTITY — Advanced Shipment Notice
// ================================================
// PURPOSE:
// Supplier notifies warehouse BEFORE truck arrives
// Warehouse prepares dock, staff, LPN labels
//
// REAL WORLD FLOW:
// Apple India ships 100 iPhones →
// Sends ASN to WH001 →
// WH001 knows what's coming before truck arrives
//
// LINKED TO:
// → Purchase Order (PO)
// → Appointment (dock booking)
// → Receiving (when truck arrives)
// → GRN (after receiving complete)
//
// TABLE: wms_asn
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_asn",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_asn_number",
                        columnNames = "asn_number"
                )
        }
)
public class Asn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // ASN NUMBER
    // ================================================
    // Auto generated
    // Example: ASN-20240122001
    // ================================================
    @Column(
            name = "asn_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String asnNumber;

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
    // SUPPLIER CODE
    // ================================================
    @Column(
            name = "supplier_code",
            nullable = false,
            length = 20
    )
    private String supplierCode;

    // ================================================
    // PO NUMBER
    // ================================================
    // ASN is always against a PO
    // ================================================
    @Column(
            name = "po_number",
            nullable = false,
            length = 30
    )
    private String poNumber;

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
            name = "asn_quantity",
            nullable = false
    )
    private Double asnQuantity;

    @Column(name = "received_quantity")
    @Builder.Default
    private Double receivedQuantity = 0.0;

    // ================================================
    // VEHICLE DETAILS
    // ================================================
    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "driver_phone", length = 15)
    private String driverPhone;

    // ================================================
    // DATES
    // ================================================
    @Column(name = "expected_date")
    private LocalDate expectedDate;

    @Column(name = "actual_arrival_date")
    private LocalDate actualArrivalDate;

    // ================================================
    // APPOINTMENT NUMBER
    // ================================================
    @Column(
            name = "appointment_number",
            length = 30
    )
    private String appointmentNumber;

    // ================================================
    // DOCK DOOR
    // ================================================
    @Column(name = "dock_door", length = 20)
    private String dockDoor;

    // ================================================
    // PALLET / CARTON COUNT
    // ================================================
    @Column(name = "pallet_count")
    private Integer palletCount;

    @Column(name = "carton_count")
    private Integer cartonCount;

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
    private ASNStatus status = ASNStatus.CREATED;
}