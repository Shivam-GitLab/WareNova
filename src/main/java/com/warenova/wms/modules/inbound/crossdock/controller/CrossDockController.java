package com.warenova.wms.modules.inbound.crossdock.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockRequest;
import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockResponse;
import com.warenova.wms.modules.inbound.crossdock.service.CrossDockService;
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
@RequestMapping("/api/inbound/crossdock")
@RequiredArgsConstructor
@Tag(
        name = "Cross Docking",
        description = "Cross dock operations APIs"
)
public class CrossDockController {

    private final CrossDockService crossDockService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create cross dock order")
    public ResponseEntity<ApiResponse<CrossDockResponse>>
    create(
            @Valid @RequestBody
            CrossDockRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Cross dock created successfully",
                        crossDockService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all cross docks")
    public ResponseEntity<ApiResponse<List<CrossDockResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross docks fetched",
                        crossDockService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cross dock by ID")
    public ResponseEntity<ApiResponse<CrossDockResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross dock fetched",
                        crossDockService.getById(id)
                ));
    }

    @GetMapping("/asn/{asnNumber}")
    @Operation(summary = "Get cross docks by ASN")
    public ResponseEntity<ApiResponse<List<CrossDockResponse>>>
    getByAsn(
            @PathVariable String asnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross docks fetched",
                        crossDockService.getByAsn(asnNumber)
                ));
    }

    @PutMapping("/{id}/process")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Process cross dock")
    public ResponseEntity<ApiResponse<CrossDockResponse>>
    process(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross dock processing started",
                        crossDockService.process(id)
                ));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Complete cross dock")
    public ResponseEntity<ApiResponse<CrossDockResponse>>
    complete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross dock completed",
                        crossDockService.complete(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel cross dock")
    public ResponseEntity<ApiResponse<CrossDockResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cross dock cancelled",
                        crossDockService.cancel(id)
                ));
    }
}
