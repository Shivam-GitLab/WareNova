package com.warenova.wms.modules.master.lpn.controller;

import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.lpn.dto.LpnRequest;
import com.warenova.wms.modules.master.lpn.dto.LpnResponse;
import com.warenova.wms.modules.master.lpn.service.LpnService;
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
// LPN CONTROLLER
// ================================================
// BASE URL: /api/master/lpns
// ================================================

@RestController
@RequestMapping("/api/master/lpns")
@RequiredArgsConstructor
@Tag(
        name = "LPN Master",
        description = "License Plate Number management APIs"
)
public class LpnController {

    private final LpnService lpnService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new LPN")
    public ResponseEntity<ApiResponse<LpnResponse>>
    create(
            @Valid @RequestBody LpnRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "LPN created successfully",
                        lpnService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all LPNs by warehouse")
    public ResponseEntity<ApiResponse<List<LpnResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPNs fetched successfully",
                        lpnService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get LPN by ID")
    public ResponseEntity<ApiResponse<LpnResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPN fetched successfully",
                        lpnService.getById(id)
                ));
    }

    @GetMapping("/number/{lpnNumber}")
    @Operation(summary = "Get LPN by number")
    public ResponseEntity<ApiResponse<LpnResponse>>
    getByNumber(
            @PathVariable String lpnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPN fetched successfully",
                        lpnService.getByLpnNumber(lpnNumber)
                ));
    }

    @GetMapping("/location/{locationCode}")
    @Operation(summary = "Get LPNs at location")
    public ResponseEntity<ApiResponse<List<LpnResponse>>>
    getByLocation(
            @PathVariable String locationCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPNs fetched successfully",
                        lpnService.getByLocation(locationCode)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}/status/{status}")
    @Operation(summary = "Get LPNs by status")
    public ResponseEntity<ApiResponse<List<LpnResponse>>>
    getByStatus(
            @PathVariable String warehouseCode,
            @PathVariable LpnStatus status) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPNs fetched successfully",
                        lpnService.getByStatus(
                                warehouseCode, status)
                ));
    }

    // ── Move LPN ─────────────────────────────────
    // Core operation used in putaway/picking
    @PutMapping("/{lpnNumber}/move")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR'," +
            "'ROLE_WAREHOUSE_STAFF')")
    @Operation(summary = "Move LPN to new location")
    public ResponseEntity<ApiResponse<LpnResponse>>
    moveLpn(
            @PathVariable String lpnNumber,
            @RequestParam String newLocation) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPN moved successfully",
                        lpnService.moveLpn(
                                lpnNumber, newLocation)
                ));
    }

    // ── Update status ─────────────────────────────
    @PutMapping("/{lpnNumber}/status")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update LPN status")
    public ResponseEntity<ApiResponse<LpnResponse>>
    updateStatus(
            @PathVariable String lpnNumber,
            @RequestParam LpnStatus status) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPN status updated",
                        lpnService.updateStatus(
                                lpnNumber, status)
                ));
    }

    // ── Cancel LPN ────────────────────────────────
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Cancel LPN")
    public ResponseEntity<ApiResponse<Void>>
    cancel(@PathVariable Long id) {

        lpnService.cancel(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "LPN cancelled successfully"
                ));
    }
}