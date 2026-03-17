package com.warenova.wms.modules.inbound.asn.dto;

import com.warenova.wms.common.enums.ASNStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsnResponse {

    private Long id;
    private String asnNumber;
    private String warehouseCode;
    private String supplierCode;
    private String poNumber;
    private String sku;
    private Double asnQuantity;
    private Double receivedQuantity;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private LocalDate expectedDate;
    private LocalDate actualArrivalDate;
    private String appointmentNumber;
    private String dockDoor;
    private Integer palletCount;
    private Integer cartonCount;
    private String notes;
    private ASNStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}