package com.warenova.wms.modules.inbound.crossdock.dto;

import com.warenova.wms.common.enums.CrossDockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrossDockResponse {

    private Long id;
    private String crossdockNumber;
    private String warehouseCode;
    private String asnNumber;
    private String poNumber;
    private String supplierCode;
    private String inboundLpn;
    private String receivingLocation;
    private String sku;
    private Double quantity;
    private String salesOrderNumber;
    private String customerCode;
    private String dispatchLocation;
    private String outboundLpn;
    private String carrierCode;
    private CrossDockStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}