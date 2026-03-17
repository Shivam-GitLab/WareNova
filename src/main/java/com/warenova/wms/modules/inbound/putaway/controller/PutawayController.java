package com.warenova.wms.modules.inbound.putaway.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.putaway.dto.PutawayRequest;
import com.warenova.wms.modules.inbound.putaway.dto.PutawayResponse;
import com.warenova.wms.modules.inbound.putaway.service.PutawayService;
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
// PUTAWAY CONTROLLER
// ================================================
// BASE URL: /api/inbound/putaway
// ================================================

@RestController
@RequestMapping("/api/inbound/putaway")
@RequiredArgsConstructor
@Tag(
        name = "Putaway",
        description = "Putaway operations APIs"
)
public class PutawayController {

    private final PutawayService putawayService;

    // ── Create putaway task ──────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create putaway task")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    create(
            @Valid @RequestBody
            PutawayRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Putaway task created successfully",
                        putawayService.create(request)
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all putaway tasks")
    public ResponseEntity<ApiResponse<List<PutawayResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway tasks fetched",
                        putawayService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    // ── Get pending tasks ────────────────────────
    @GetMapping("/warehouse/{warehouseCode}/pending")
    @Operation(summary = "Get pending putaway tasks")
    public ResponseEntity<ApiResponse<List<PutawayResponse>>>
    getPending(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Pending tasks fetched",
                        putawayService.getPendingTasks(
                                warehouseCode)
                ));
    }

    // ── Get tasks for staff ──────────────────────
    @GetMapping("/warehouse/{warehouseCode}/staff/{assignedTo}")
    @Operation(summary = "Get tasks for staff")
    public ResponseEntity<ApiResponse<List<PutawayResponse>>>
    getForStaff(
            @PathVariable String warehouseCode,
            @PathVariable String assignedTo) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Staff tasks fetched",
                        putawayService.getTasksForStaff(
                                warehouseCode, assignedTo)
                ));
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get putaway by ID")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway fetched",
                        putawayService.getById(id)
                ));
    }

    // ── Get by putaway number ────────────────────
    @GetMapping("/number/{putawayNumber}")
    @Operation(summary = "Get by putaway number")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    getByNumber(
            @PathVariable String putawayNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway fetched",
                        putawayService.getByPutawayNumber(
                                putawayNumber)
                ));
    }

    // ── Get by receiving number ──────────────────
    @GetMapping("/receiving/{receivingNumber}")
    @Operation(summary = "Get by receiving number")
    public ResponseEntity<ApiResponse<List<PutawayResponse>>>
    getByReceiving(
            @PathVariable String receivingNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway tasks fetched",
                        putawayService.getByReceivingNumber(
                                receivingNumber)
                ));
    }

    // ── Suggest location ─────────────────────────
    @GetMapping("/suggest-location")
    @Operation(summary = "Suggest putaway location")
    public ResponseEntity<ApiResponse<String>>
    suggestLocation(
            @RequestParam String warehouseCode,
            @RequestParam String sku) {

        String location =
                putawayService.suggestLocation(
                        warehouseCode, sku);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Location suggested",
                        location != null
                                ? location
                                : "No empty location found"
                ));
    }

    // ── Start putaway ────────────────────────────
    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR'," +
            "'ROLE_WAREHOUSE_STAFF')")
    @Operation(summary = "Start putaway")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    start(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway started",
                        putawayService.startPutaway(id)
                ));
    }

    // ── Complete putaway ─────────────────────────
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR'," +
            "'ROLE_WAREHOUSE_STAFF')")
    @Operation(summary = "Complete putaway")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    complete(
            @PathVariable Long id,
            @RequestParam String toLocation) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway completed successfully",
                        putawayService.completePutaway(
                                id, toLocation)
                ));
    }

    // ── Cancel putaway ───────────────────────────
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel putaway")
    public ResponseEntity<ApiResponse<PutawayResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Putaway cancelled",
                        putawayService.cancel(id)
                ));
    }
}
