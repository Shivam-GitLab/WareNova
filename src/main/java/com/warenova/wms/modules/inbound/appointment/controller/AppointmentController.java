package com.warenova.wms.modules.inbound.appointment.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.appointment.dto.AppointmentRequest;
import com.warenova.wms.modules.inbound.appointment.dto.AppointmentResponse;
import com.warenova.wms.modules.inbound.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound/appointments")
@RequiredArgsConstructor
@Tag(
        name = "Appointment Scheduling",
        description = "Inbound truck appointment APIs"
)
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Schedule appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    create(
            @Valid @RequestBody
            AppointmentRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Appointment scheduled successfully",
                        appointmentService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointments fetched",
                        appointmentService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}/today")
    @Operation(summary = "Get today's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>>
    getToday(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Today's appointments fetched",
                        appointmentService.getTodaysAppointments(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment fetched",
                        appointmentService.getById(id)
                ));
    }

    @PutMapping("/{id}/truck-arrived")
    @Operation(summary = "Mark truck as arrived")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    truckArrived(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Truck arrival recorded",
                        appointmentService.truckArrived(id)
                ));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    complete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment completed",
                        appointmentService.complete(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment cancelled",
                        appointmentService.cancel(id)
                ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody
            AppointmentRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment updated",
                        appointmentService.update(id, request)
                ));
    }
}
