package com.warenova.wms.modules.master.lpn.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.common.enums.LpnType;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// LPN ENTITY — License Plate Number
// ================================================
// PURPOSE:
// Tracks containers (pallets/cartons/totes)
// as they move through warehouse
//
// REAL WORLD EXAMPLE:
// LPN001 = Pallet with 50 iPhones at A-01-01-01
// Scan LPN001 = move ALL 50 iPhones at once
//
// USED IN:
// → Receiving  (assign LPN to received goods)
// → Putaway    (move LPN to bin location)
// → Picking    (pick from LPN)
// → Shipping   (ship LPN)
//
// TABLE: wms_lpn
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_lpn",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lpn_number",
                        columnNames = "lpn_number"
                )
        }
)
public class Lpn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // LPN NUMBER
    // ================================================
    // Unique barcode on pallet/carton
    // Example: LPN-2024-000001
    // Scanned by RF gun in warehouse
    // ================================================
    @Column(
            name = "lpn_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String lpnNumber;

    // ================================================
    // LPN TYPE
    // ================================================
    // PALLET / CARTON / TOTE / LOOSE
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "lpn_type",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private LpnType lpnType = LpnType.CARTON;

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
    // CURRENT LOCATION
    // ================================================
    // Where LPN is right now in warehouse
    // Updated every time LPN moves
    // Example: DOCK-01, STAGE-01, A-01-01-01
    // ================================================
    @Column(
            name = "current_location",
            length = 30
    )
    private String currentLocation;

    // ================================================
    // PARENT LPN
    // ================================================
    // LPN inside another LPN
    // Example: Carton LPN inside Pallet LPN
    // ================================================
    @Column(
            name = "parent_lpn_number",
            length = 30
    )
    private String parentLpnNumber;

    // ================================================
    // ASN NUMBER
    // ================================================
    // Which ASN this LPN came from
    // Set during receiving
    // ================================================
    @Column(name = "asn_number", length = 30)
    private String asnNumber;

    // ================================================
    // PO NUMBER
    // ================================================
    @Column(name = "po_number", length = 30)
    private String poNumber;

    // ================================================
    // WEIGHT / DIMENSIONS
    // ================================================
    @Column(name = "gross_weight_kg")
    private Double grossWeightKg;

    @Column(name = "length_cm")
    private Double lengthCm;

    @Column(name = "width_cm")
    private Double widthCm;

    @Column(name = "height_cm")
    private Double heightCm;

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
    private LpnStatus status = LpnStatus.EMPTY;
}