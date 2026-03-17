package com.warenova.wms.modules.inbound.purchaseorder.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderRequest;
import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderResponse;
import com.warenova.wms.modules.inbound.purchaseorder.service.PurchaseOrderService;
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
@RequestMapping("/api/inbound/purchase-orders")
@RequiredArgsConstructor
@Tag(
        name = "Purchase Orders",
        description = "Purchase Order management APIs"
)
public class PurchaseOrderController {

    private final PurchaseOrderService poService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create Purchase Order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    create(
            @Valid @RequestBody
            PurchaseOrderRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "PO created successfully",
                        poService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all POs by warehouse")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "POs fetched successfully",
                        poService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get PO by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO fetched",
                        poService.getById(id)
                ));
    }

    @GetMapping("/number/{poNumber}")
    @Operation(summary = "Get PO by number")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    getByNumber(
            @PathVariable String poNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO fetched",
                        poService.getByPoNumber(poNumber)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}/status/{status}")
    @Operation(summary = "Get POs by status")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>>
    getByStatus(
            @PathVariable String warehouseCode,
            @PathVariable String status) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "POs fetched",
                        poService.getByStatus(
                                warehouseCode, status)
                ));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Confirm PO")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    confirm(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO confirmed successfully",
                        poService.confirm(id)
                ));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Close PO")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    close(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO closed successfully",
                        poService.close(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel PO")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO cancelled successfully",
                        poService.cancel(id)
                ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update PO")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody
            PurchaseOrderRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "PO updated successfully",
                        poService.update(id, request)
                ));
    }
}
