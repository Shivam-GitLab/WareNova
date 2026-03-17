package com.warenova.wms.modules.inbound.purchaseorder.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.POStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

// ================================================
// PURCHASE ORDER ENTITY
// ================================================
// PURPOSE:
// Order placed by warehouse/buyer to supplier
// to procure goods
//
// REAL WORLD FLOW:
// Inventory low → Create PO → Send to Supplier
// → Supplier ships → ASN created → Goods arrive
//
// TABLE: wms_purchase_order
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_purchase_order",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_po_number",
                        columnNames = "po_number"
                )
        }
)
public class PurchaseOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ================================================
    // PO NUMBER
    // ================================================
    // Auto generated unique number
    // Example: PO-20240115-001
    // ================================================
    @Column(
            name = "po_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String poNumber;

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
            name = "ordered_quantity",
            nullable = false
    )
    private Double orderedQuantity;

    @Column(name = "received_quantity")
    @Builder.Default
    private Double receivedQuantity = 0.0;

    @Column(name = "pending_quantity")
    private Double pendingQuantity;

    // ================================================
    // UNIT PRICE
    // ================================================
    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total_amount")
    private Double totalAmount;

    // ================================================
    // DATES
    // ================================================
    @Column(
            name = "order_date",
            nullable = false
    )
    private LocalDate orderDate;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDate actualDeliveryDate;

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
            length = 30
    )
    @Builder.Default
    private POStatus status = POStatus.DRAFT;
}