package com.warenova.wms.modules.master.warehouse.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.warehouse.dto.WarehouseRequest;
import com.warenova.wms.modules.master.warehouse.dto.WarehouseResponse;
import com.warenova.wms.modules.master.warehouse.service.WarehouseService;
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
// WAREHOUSE CONTROLLER
// ================================================
// REST APIs for Warehouse Master Data
// BASE URL: /api/master/warehouses
//
// ACCESS CONTROL:
// GET  → All authenticated users
// POST → Admin + Supervisor only
// PUT  → Admin + Supervisor only
// DELETE → Admin only
// ================================================

@RestController
@RequestMapping("/api/master/warehouses")
@RequiredArgsConstructor
@Tag(
        name = "Warehouse Master",
        description = "Warehouse management APIs"
)
public class WarehouseController {

    private final WarehouseService warehouseService;

    // ================================================
    // CREATE WAREHOUSE
    // POST /api/master/warehouses
    // ================================================
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new warehouse")
    public ResponseEntity<ApiResponse<WarehouseResponse>>
    create(
            @Valid @RequestBody
            WarehouseRequest request) {

        WarehouseResponse response =
                warehouseService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Warehouse created successfully",
                        response
                ));
    }

    // ================================================
    // GET ALL WAREHOUSES
    // GET /api/master/warehouses
    // ================================================
    @GetMapping
    @Operation(summary = "Get all warehouses")
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>>
    getAll() {

        List<WarehouseResponse> response =
                warehouseService.getAll();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouses fetched successfully",
                        response
                )
        );
    }

    // ================================================
    // GET ALL ACTIVE WAREHOUSES
    // GET /api/master/warehouses/active
    // ================================================
    @GetMapping("/active")
    @Operation(summary = "Get all active warehouses")
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>>
    getAllActive() {

        List<WarehouseResponse> response =
                warehouseService.getAllActive();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active warehouses fetched",
                        response
                )
        );
    }

    // ================================================
    // GET WAREHOUSE BY ID
    // GET /api/master/warehouses/{id}
    // ================================================
    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<ApiResponse<WarehouseResponse>>
    getById(@PathVariable Long id) {

        WarehouseResponse response =
                warehouseService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouse fetched successfully",
                        response
                )
        );
    }

    // ================================================
    // GET WAREHOUSE BY CODE
    // GET /api/master/warehouses/code/{warehouseCode}
    // ================================================
    @GetMapping("/code/{warehouseCode}")
    @Operation(summary = "Get warehouse by code")
    public ResponseEntity<ApiResponse<WarehouseResponse>>
    getByCode(
            @PathVariable String warehouseCode) {

        WarehouseResponse response =
                warehouseService.getByCode(warehouseCode);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouse fetched successfully",
                        response
                )
        );
    }

    // ================================================
    // UPDATE WAREHOUSE
    // PUT /api/master/warehouses/{id}
    // ================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update warehouse")
    public ResponseEntity<ApiResponse<WarehouseResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {

        WarehouseResponse response =
                warehouseService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouse updated successfully",
                        response
                )
        );
    }

    // ================================================
    // DEACTIVATE WAREHOUSE
    // DELETE /api/master/warehouses/{id}
    // ================================================
    // Note: This is SOFT DELETE
    // Sets status = INACTIVE
    // Data is never permanently deleted in WMS!
    // ================================================


    // ── Activate warehouse ───────────────────────
// CHANGE @PatchMapping → @PutMapping
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Activate warehouse")
    public ResponseEntity<ApiResponse<Void>>
    activate(@PathVariable Long id) {

        warehouseService.activate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouse activated successfully"
                )
        );
    }

    // ── Deactivate warehouse ─────────────────────
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate warehouse")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        warehouseService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Warehouse deactivated successfully"
                )
        );
    }


}
