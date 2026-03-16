package com.warenova.wms.modules.master.customer.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// CUSTOMER ENTITY
// ================================================
// PURPOSE:
// Represents a customer who receives shipments
// from the warehouse via Sales Orders
//
// EXTENDS BaseEntity:
// Auto gets created_at, updated_at,
// created_by, updated_by columns
//
// REAL WORLD EXAMPLE (from Infor WMS):
// CUS001 = Reliance Retail Ltd
// CUS002 = Amazon India
// CUS003 = Flipkart Pvt Ltd
//
// Customer is linked to:
// → Sales Orders (outbound)
// → Shipments
//
// TABLE: wms_customer
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_customer_code",
                        columnNames = "customer_code"
                )
        }
)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // CUSTOMER CODE
    // ================================================
    // Unique identifier for customer
    // Example: CUS001, AMAZON-IND
    // ================================================
    @Column(
            name = "customer_code",
            nullable = false,
            unique = true,
            length = 20
    )
    private String customerCode;

    // ================================================
    // CUSTOMER NAME
    // ================================================
    @Column(
            name = "customer_name",
            nullable = false,
            length = 200
    )
    private String customerName;

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
    // SHIPPING ADDRESS
    // ================================================
    // Default delivery address
    // Can be overridden per order
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
    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    // ================================================
    // CREDIT LIMIT
    // ================================================
    // Maximum credit allowed for this customer
    // Used in billing module
    // ================================================
    @Column(name = "credit_limit")
    private Double creditLimit;

    // ================================================
    // PAYMENT TERMS
    // ================================================
    @Column(name = "payment_terms_days")
    private Integer paymentTermsDays;

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
    // ACTIVE / INACTIVE / SUSPENDED
    // ================================================
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private CustomerStatus status =
            CustomerStatus.ACTIVE;
}