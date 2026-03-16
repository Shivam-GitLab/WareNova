package com.warenova.wms.modules.master.supplier.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.SupplierStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// SUPPLIER ENTITY
// ================================================
// PURPOSE:
// Represents a vendor/supplier who sends goods
// to the warehouse via Purchase Orders
//
// EXTENDS BaseEntity:
// Auto gets created_at, updated_at,
// created_by, updated_by columns
//
// REAL WORLD EXAMPLE (from Infor WMS):
// SUP001 = Apple India Pvt Ltd
// SUP002 = Samsung Electronics
// SUP003 = Nike India
//
// Supplier is linked to:
// → Purchase Orders (inbound)
// → ASN (Advanced Shipment Notice)
//
// TABLE: wms_supplier
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_supplier",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_supplier_code",
                        columnNames = "supplier_code"
                )
        }
)
public class Supplier extends BaseEntity {

    // ================================================
    // PRIMARY KEY
    // ================================================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // SUPPLIER CODE
    // ================================================
    // Unique identifier for supplier
    // Example: SUP001, APPLE-IND, VENDOR-001
    // ================================================
    @Column(
            name = "supplier_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String supplierCode;

    // ================================================
    // SUPPLIER NAME
    // ================================================
    // Full legal name of supplier
    // Example: "Apple India Private Limited"
    // ================================================
    @Column(
            name = "supplier_name",
            nullable = false,
            length = 200
    )
    private String supplierName;

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
    // ADDRESS
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
    // GST / TAX NUMBER
    // ================================================
    // Indian GST number for tax compliance
    // Example: 27AAPFU0939F1ZV
    // ================================================
    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    // ================================================
    // PAN NUMBER
    // ================================================
    @Column(name = "pan_number", length = 10)
    private String panNumber;

    // ================================================
    // PAYMENT TERMS
    // ================================================
    // Number of days for payment
    // Example: 30, 45, 60
    // ================================================
    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays;

    // ================================================
    // LEAD TIME
    // ================================================
    // Days from PO to delivery
    // Used for reorder planning
    // ================================================
    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

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
    // STATUS
    // ================================================
    // ACTIVE / INACTIVE / BLACKLISTED
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private SupplierStatus status =
            SupplierStatus.ACTIVE;
}