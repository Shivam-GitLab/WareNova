package com.warenova.wms.modules.master.customer.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.customer.dto.CustomerRequest;
import com.warenova.wms.modules.master.customer.dto.CustomerResponse;
import com.warenova.wms.modules.master.customer.service.CustomerService;
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
// CUSTOMER CONTROLLER
// ================================================
// BASE URL: /api/master/customers
// ================================================

@RestController
@RequestMapping("/api/master/customers")
@RequiredArgsConstructor
@Tag(
        name = "Customer Master",
        description = "Customer management APIs"
)
public class CustomerController {

    private final CustomerService customerService;

    // ── Create customer ──────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new customer")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    create(
            @Valid @RequestBody
            CustomerRequest request) {

        CustomerResponse response =
                customerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Customer created successfully",
                        response
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customers fetched successfully",
                        customerService.getAllByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get active by warehouse ──────────────────
    @GetMapping("/warehouse/{warehouseCode}/active")
    @Operation(summary = "Get active customers")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>>
    getAllActive(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Active customers fetched",
                        customerService.getAllActiveByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer fetched successfully",
                        customerService.getById(id)
                )
        );
    }

    // ── Get by code ──────────────────────────────
    @GetMapping("/code/{customerCode}")
    @Operation(summary = "Get customer by code")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    getByCode(
            @PathVariable String customerCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer fetched successfully",
                        customerService.getByCode(customerCode)
                )
        );
    }

    // ── Update customer ──────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update customer")
    public ResponseEntity<ApiResponse<CustomerResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer updated successfully",
                        customerService.update(id, request)
                )
        );
    }

    // ── Deactivate customer ──────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate customer")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        customerService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Customer deactivated successfully"
                )
        );
    }
}
