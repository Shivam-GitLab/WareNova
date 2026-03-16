package com.warenova.wms.modules.inbound.grn.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.grn.dto.GrnRequest;
import com.warenova.wms.modules.inbound.grn.dto.GrnResponse;
import com.warenova.wms.modules.inbound.grn.service.GrnService;
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
@RequestMapping("/api/inbound/grns")
@RequiredArgsConstructor
@Tag(
        name = "GRN",
        description = "Goods Receipt Note APIs"
)
public class GrnController {

    private final GrnService grnService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create GRN")
    public ResponseEntity<ApiResponse<GrnResponse>>
    create(
            @Valid @RequestBody GrnRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "GRN created successfully",
                        grnService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all GRNs by warehouse")
    public ResponseEntity<ApiResponse<List<GrnResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRNs fetched successfully",
                        grnService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get GRN by ID")
    public ResponseEntity<ApiResponse<GrnResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN fetched",
                        grnService.getById(id)
                ));
    }

    @GetMapping("/number/{grnNumber}")
    @Operation(summary = "Get GRN by number")
    public ResponseEntity<ApiResponse<GrnResponse>>
    getByNumber(
            @PathVariable String grnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN fetched",
                        grnService.getByGrnNumber(grnNumber)
                ));
    }

    @GetMapping("/asn/{asnNumber}")
    @Operation(summary = "Get GRN by ASN number")
    public ResponseEntity<ApiResponse<GrnResponse>>
    getByAsn(
            @PathVariable String asnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN fetched",
                        grnService.getByAsnNumber(asnNumber)
                ));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Confirm GRN")
    public ResponseEntity<ApiResponse<GrnResponse>>
    confirm(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN confirmed",
                        grnService.confirm(id)
                ));
    }

    @PutMapping("/{id}/post")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Post GRN to inventory")
    public ResponseEntity<ApiResponse<GrnResponse>>
    post(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN posted to inventory",
                        grnService.post(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel GRN")
    public ResponseEntity<ApiResponse<GrnResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "GRN cancelled",
                        grnService.cancel(id)
                ));
    }
}