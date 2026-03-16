package com.warenova.wms.modules.inbound.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Supplier code is required")
    private String supplierCode;

    @NotBlank(message = "Dock door is required")
    private String dockDoor;

    @NotNull(message = "Expected arrival is required")
    @Future(message = "Arrival must be future date")
    private LocalDateTime expectedArrival;

    private String asnNumber;
    private String poNumber;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private Integer expectedPallets;
    private Integer expectedCartons;
    private String notes;
}