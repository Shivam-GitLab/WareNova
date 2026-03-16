package com.warenova.wms.modules.inbound.crossdock.entity;

import com.warenova.wms.common.BaseEntity;
import com.warenova.wms.common.enums.CrossDockStatus;
import jakarta.persistence.*;
import lombok.*;

// ================================================
// CROSS DOCK ENTITY
// ================================================
// PURPOSE:
// Direct flow from inbound to outbound
// NO storage in between
// Stage → Lane ID → Ship
//
// REAL WORLD EXAMPLE:
// Amazon Flash Sale items
// → Received at DOCK-01
// → Directly moved to Lane-05 (dispatch)
// → Shipped same day, never stored!
//
// TABLE: wms_cross_dock
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "wms_cross_dock",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_crossdock_number",
                        columnNames = "crossdock_number"
                )
        }
)
public class CrossDock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(
            name = "crossdock_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String crossdockNumber;

    @Column(
            name = "warehouse_code",
            nullable = false,
            length = 20
    )
    private String warehouseCode;

    // ── Inbound side ──────────────────────────────
    @Column(name = "asn_number", length = 30)
    private String asnNumber;

    @Column(name = "po_number", length = 30)
    private String poNumber;

    @Column(name = "supplier_code", length = 20)
    private String supplierCode;

    @Column(name = "inbound_lpn", length = 30)
    private String inboundLpn;

    @Column(
            name = "receiving_location",
            length = 30
    )
    private String receivingLocation;

    // ── Item details ──────────────────────────────
    @Column(name = "sku", length = 50)
    private String sku;

    @Column(name = "quantity")
    private Double quantity;

    // ── Outbound side ─────────────────────────────
    @Column(
            name = "sales_order_number",
            length = 30
    )
    private String salesOrderNumber;

    @Column(name = "customer_code", length = 20)
    private String customerCode;

    @Column(
            name = "dispatch_location",
            length = 30
    )
    private String dispatchLocation;

    @Column(name = "outbound_lpn", length = 30)
    private String outboundLpn;

    @Column(name = "carrier_code", length = 20)
    private String carrierCode;

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
    private CrossDockStatus status =
            CrossDockStatus.PENDING;
}