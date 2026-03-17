package com.warenova.wms.modules.inbound.asn.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.asn.dto.AsnRequest;
import com.warenova.wms.modules.inbound.asn.dto.AsnResponse;
import com.warenova.wms.modules.inbound.asn.service.AsnService;
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
@RequestMapping("/api/inbound/asns")
@RequiredArgsConstructor
@Tag(
        name = "ASN",
        description = "Advanced Shipment Notice APIs"
)
public class AsnController {

    private final AsnService asnService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create ASN")
    public ResponseEntity<ApiResponse<AsnResponse>>
    create(
            @Valid @RequestBody AsnRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "ASN created successfully",
                        asnService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all ASNs by warehouse")
    public ResponseEntity<ApiResponse<List<AsnResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASNs fetched successfully",
                        asnService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ASN by ID")
    public ResponseEntity<ApiResponse<AsnResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN fetched",
                        asnService.getById(id)
                ));
    }

    @GetMapping("/number/{asnNumber}")
    @Operation(summary = "Get ASN by number")
    public ResponseEntity<ApiResponse<AsnResponse>>
    getByNumber(
            @PathVariable String asnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN fetched",
                        asnService.getByAsnNumber(asnNumber)
                ));
    }

    @GetMapping("/po/{poNumber}")
    @Operation(summary = "Get ASNs by PO number")
    public ResponseEntity<ApiResponse<List<AsnResponse>>>
    getByPo(
            @PathVariable String poNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASNs fetched",
                        asnService.getByPoNumber(poNumber)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}/status/{status}")
    @Operation(summary = "Get ASNs by status")
    public ResponseEntity<ApiResponse<List<AsnResponse>>>
    getByStatus(
            @PathVariable String warehouseCode,
            @PathVariable String status) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASNs fetched",
                        asnService.getByStatus(
                                warehouseCode, status)
                ));
    }

    @PutMapping("/{id}/in-transit")
    @Operation(summary = "Mark ASN in transit")
    public ResponseEntity<ApiResponse<AsnResponse>>
    markInTransit(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN marked IN_TRANSIT",
                        asnService.markInTransit(id)
                ));
    }

    @PutMapping("/{id}/arrived")
    @Operation(summary = "Mark ASN arrived")
    public ResponseEntity<ApiResponse<AsnResponse>>
    markArrived(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN marked ARRIVED",
                        asnService.markArrived(id)
                ));
    }

    @PutMapping("/{id}/start-receiving")
    @Operation(summary = "Start receiving")
    public ResponseEntity<ApiResponse<AsnResponse>>
    startReceiving(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving started",
                        asnService.startReceiving(id)
                ));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Close ASN")
    public ResponseEntity<ApiResponse<AsnResponse>>
    close(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN closed successfully",
                        asnService.close(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel ASN")
    public ResponseEntity<ApiResponse<AsnResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN cancelled",
                        asnService.cancel(id)
                ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update ASN")
    public ResponseEntity<ApiResponse<AsnResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody AsnRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "ASN updated successfully",
                        asnService.update(id, request)
                ));
    }
}
