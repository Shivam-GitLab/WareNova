package com.warenova.wms.modules.inbound.appointment.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// ================================================
// APPOINTMENT ENTITY
// ================================================
// PURPOSE:
// Schedule inbound truck arrivals in advance
// Assign dock doors to incoming shipments
// Prevents congestion at receiving dock
//
// REAL WORLD EXAMPLE:
// APT001 → SUP001 truck arrives at DOCK-01
//          on 2024-01-15 at 10:00 AM
//          for ASN ASN001 (500 iPhones)
//
// TABLE: wms_appointment
// ================================================

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_appointment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_appointment_number",
                        columnNames = "appointment_number"
                )
        }
)
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // APPOINTMENT NUMBER
    // ================================================
    @Column(
            name = "appointment_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String appointmentNumber;

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
    // DOCK DOOR
    // ================================================
    // Which dock door truck will use
    // Example: DOCK-01, DOCK-02
    // ================================================
    @Column(
            name = "dock_door",
            nullable = false,
            length = 20
    )
    private String dockDoor;

    // ================================================
    // EXPECTED ARRIVAL TIME
    // ================================================
    @Column(
            name = "expected_arrival",
            nullable = false
    )
    private LocalDateTime expectedArrival;

    // ================================================
    // ACTUAL ARRIVAL TIME
    // ================================================
    // Set when truck actually arrives
    // ================================================
    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    // ================================================
    // ASN NUMBER
    // ================================================
    // Which ASN this appointment is for
    // ================================================
    @Column(name = "asn_number", length = 30)
    private String asnNumber;

    // ================================================
    // PO NUMBER
    // ================================================
    @Column(name = "po_number", length = 30)
    private String poNumber;

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
    // EXPECTED PALLETS / CARTONS
    // ================================================
    @Column(name = "expected_pallets")
    private Integer expectedPallets;

    @Column(name = "expected_cartons")
    private Integer expectedCartons;

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
    private AppointmentStatus status =
            AppointmentStatus.SCHEDULED;
}