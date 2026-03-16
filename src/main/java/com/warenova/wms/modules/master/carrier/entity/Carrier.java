package com.warenova.wms.modules.master.carrier.entity;

import com.warenova.wms.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// CARRIER ENTITY
// ================================================
// PURPOSE:
// Represents a shipping/logistics company
// that delivers orders to customers
//
// EXTENDS BaseEntity:
// Auto gets created_at, updated_at,
// created_by, updated_by columns
//
// REAL WORLD EXAMPLE (from Infor WMS):
// CAR001 = FedEx India
// CAR002 = Delhivery
// CAR003 = Blue Dart
// CAR004 = Ecom Express
// CAR005 = DTDC
//
// Carrier is linked to:
// → Shipments (outbound)
// → Manifests
//
// TABLE: wms_carrier
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_carrier",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_carrier_code",
                        columnNames = "carrier_code"
                )
        }
)
public class Carrier extends BaseEntity {

    // ================================================
    // PRIMARY KEY
    // ================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // CARRIER CODE
    // ================================================
    // Unique identifier for carrier
    // Example: CAR001, FEDEX, DELHIVERY
    // ================================================
    @Column(
            name = "carrier_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String carrierCode;

    // ================================================
    // CARRIER NAME
    // ================================================
    // Full name of carrier company
    // Example: "FedEx India Private Limited"
    // ================================================
    @Column(
            name = "carrier_name",
            nullable = false,
            length = 200
    )
    private String carrierName;

    // ================================================
    // CARRIER TYPE
    // ================================================
    // Type of carrier service
    // Example: ROAD, AIR, SEA, COURIER
    // ================================================
    @Column(
            name = "carrier_type",
            length = 20
    )
    private String carrierType;

    // ================================================
    // CONTACT DETAILS
    // ================================================
    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", length = 15)
    private String contactPhone;

    // ================================================
    // TRACKING URL
    // ================================================
    // URL to track shipment
    // Example: https://track.delhivery.com/?id=
    // Append tracking number at end
    // ================================================
    @Column(name = "tracking_url", length = 300)
    private String trackingUrl;

    // ================================================
    // ACCOUNT NUMBER
    // ================================================
    // Our account number with carrier
    // Used for billing with carrier
    // ================================================
    @Column(name = "account_number", length = 50)
    private String accountNumber;

    // ================================================
    // SERVICE TYPES
    // ================================================
    // Comma separated service types
    // Example: "STANDARD,EXPRESS,OVERNIGHT"
    // ================================================
    @Column(name = "service_types", length = 200)
    private String serviceTypes;

    // ================================================
    // MAX WEIGHT PER SHIPMENT (KG)
    // ================================================
    @Column(name = "max_weight_kg")
    private Double maxWeightKg;

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
    // ACTIVE FLAG
    // ================================================
    @Column(name = "is_active")
    @Builder.Default
    private Boolean active = true;
}