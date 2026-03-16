package com.warenova.wms.modules.inbound.appointment.dto;

import com.warenova.wms.common.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String appointmentNumber;
    private String warehouseCode;
    private String supplierCode;
    private String dockDoor;
    private LocalDateTime expectedArrival;
    private LocalDateTime actualArrival;
    private String asnNumber;
    private String poNumber;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private Integer expectedPallets;
    private Integer expectedCartons;
    private String notes;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}