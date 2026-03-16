package com.warenova.wms.modules.master.carrier.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.carrier.dto.CarrierRequest;
import com.warenova.wms.modules.master.carrier.dto.CarrierResponse;
import com.warenova.wms.modules.master.carrier.service.CarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ================================================
// CARRIER CONTROLLER
// ================================================
// BASE URL: /api/master/carriers
// ================================================

@RestController
@RequestMapping("/api/master/carriers")
@RequiredArgsConstructor
@Tag(
        name = "Carrier Master",
        description = "Carrier/Logistics management APIs"
)
public class CarrierController {

    private final CarrierService carrierService;

    // ── Create carrier ───────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new carrier")
    public ResponseEntity<ApiResponse<CarrierResponse>>
    create(
            @Valid @RequestBody
            CarrierRequest request) {

        CarrierResponse response =
                carrierService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Carrier created successfully",
                        response
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all carriers")
    public ResponseEntity<ApiResponse<List<CarrierResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Carriers fetched successfully",
                        carrierService.getAllByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get active by warehouse ──────────────────
    @GetMapping("/warehouse/{warehouseCode}/active")
    @Operation(summary = "Get active carriers")
    public ResponseEntity<ApiResponse<List<CarrierResponse>>>
    getAllActive(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active carriers fetched",
                        carrierService.getAllActiveByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get carrier by ID")
    public ResponseEntity<ApiResponse<CarrierResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Carrier fetched successfully",
                        carrierService.getById(id)
                )
        );
    }

    // ── Get by code ──────────────────────────────
    @GetMapping("/code/{carrierCode}")
    @Operation(summary = "Get carrier by code")
    public ResponseEntity<ApiResponse<CarrierResponse>>
    getByCode(
            @PathVariable String carrierCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Carrier fetched successfully",
                        carrierService.getByCode(carrierCode)
                )
        );
    }

    // ── Update carrier ───────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update carrier")
    public ResponseEntity<ApiResponse<CarrierResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody CarrierRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Carrier updated successfully",
                        carrierService.update(id, request)
                )
        );
    }

    // ── Deactivate carrier ───────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate carrier")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        carrierService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Carrier deactivated successfully"
                )
        );
    }
}
