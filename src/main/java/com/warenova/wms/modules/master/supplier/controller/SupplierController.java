package com.warenova.wms.modules.master.supplier.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.supplier.dto.SupplierRequest;
import com.warenova.wms.modules.master.supplier.dto.SupplierResponse;
import com.warenova.wms.modules.master.supplier.service.SupplierService;
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
// SUPPLIER CONTROLLER
// ================================================
// BASE URL: /api/master/suppliers
// ================================================

@RestController
@RequestMapping("/api/master/suppliers")
@RequiredArgsConstructor
@Tag(
        name = "Supplier Master",
        description = "Supplier/Vendor management APIs"
)
public class SupplierController {

    private final SupplierService supplierService;

    // ── Create supplier ──────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new supplier")
    public ResponseEntity<ApiResponse<SupplierResponse>>
    create(
            @Valid @RequestBody
            SupplierRequest request) {

        SupplierResponse response =
                supplierService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Supplier created successfully",
                        response
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Suppliers fetched successfully",
                        supplierService.getAllByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get active by warehouse ──────────────────
    @GetMapping("/warehouse/{warehouseCode}/active")
    @Operation(summary = "Get active suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>>
    getAllActive(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active suppliers fetched",
                        supplierService.getAllActiveByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public ResponseEntity<ApiResponse<SupplierResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier fetched successfully",
                        supplierService.getById(id)
                )
        );
    }

    // ── Get by code ──────────────────────────────
    @GetMapping("/code/{supplierCode}")
    @Operation(summary = "Get supplier by code")
    public ResponseEntity<ApiResponse<SupplierResponse>>
    getByCode(
            @PathVariable String supplierCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier fetched successfully",
                        supplierService.getByCode(supplierCode)
                )
        );
    }

    // ── Update supplier ──────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update supplier")
    public ResponseEntity<ApiResponse<SupplierResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier updated successfully",
                        supplierService.update(id, request)
                )
        );
    }

    // ── Deactivate supplier ──────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate supplier")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        supplierService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Supplier deactivated successfully"
                )
        );
    }
}
