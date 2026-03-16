package com.warenova.wms.modules.master.location.controller;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.location.dto.LocationRequest;
import com.warenova.wms.modules.master.location.dto.LocationResponse;
import com.warenova.wms.modules.master.location.service.LocationService;
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
// LOCATION CONTROLLER
// ================================================
// BASE URL: /api/master/locations
// ================================================

@RestController
@RequestMapping("/api/master/locations")
@RequiredArgsConstructor
@Tag(
        name = "Location Master",
        description = "Warehouse location management APIs"
)
public class LocationController {

    private final LocationService locationService;

    // ── Create location ──────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new location")
    public ResponseEntity<ApiResponse<LocationResponse>>
    create(
            @Valid @RequestBody
            LocationRequest request) {

        LocationResponse response =
                locationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Location created successfully",
                        response
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all locations by warehouse")
    public ResponseEntity<ApiResponse<List<LocationResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        List<LocationResponse> response =
                locationService.getAllByWarehouse(
                        warehouseCode);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Locations fetched successfully",
                        response
                )
        );
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID")
    public ResponseEntity<ApiResponse<LocationResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Location fetched successfully",
                        locationService.getById(id)
                )
        );
    }

    // ── Get by status ────────────────────────────
    @GetMapping("/warehouse/{warehouseCode}/status/{status}")
    @Operation(summary = "Get locations by status")
    public ResponseEntity<ApiResponse<List<LocationResponse>>>
    getByStatus(
            @PathVariable String warehouseCode,
            @PathVariable LocationStatus status) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Locations fetched successfully",
                        locationService.getByStatus(
                                warehouseCode, status)
                )
        );
    }

    // ── Update location ──────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update location")
    public ResponseEntity<ApiResponse<LocationResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Location updated successfully",
                        locationService.update(id, request)
                )
        );
    }

    // ── Deactivate ───────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate location")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        locationService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Location deactivated successfully"
                )
        );
    }
}
