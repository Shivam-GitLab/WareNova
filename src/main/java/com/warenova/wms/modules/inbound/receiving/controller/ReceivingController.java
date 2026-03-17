package com.warenova.wms.modules.inbound.receiving.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.inbound.receiving.dto.ReceivingRequest;
import com.warenova.wms.modules.inbound.receiving.dto.ReceivingResponse;
import com.warenova.wms.modules.inbound.receiving.service.ReceivingService;
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
@RequestMapping("/api/inbound/receivings")
@RequiredArgsConstructor
@Tag(
        name = "Receiving",
        description = "Goods receiving APIs"
)
public class ReceivingController {

    private final ReceivingService receivingService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create receiving")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    create(
            @Valid @RequestBody
            ReceivingRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Receiving created successfully",
                        receivingService.create(request)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all receivings")
    public ResponseEntity<ApiResponse<List<ReceivingResponse>>>
    getAll(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receivings fetched",
                        receivingService.getAllByWarehouse(
                                warehouseCode)
                ));
    }

    @GetMapping("/warehouse/{warehouseCode}/today")
    @Operation(summary = "Get today's receivings")
    public ResponseEntity<ApiResponse<List<ReceivingResponse>>>
    getToday(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Today's receivings fetched",
                        receivingService.getTodaysReceivings(
                                warehouseCode)
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get receiving by ID")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving fetched",
                        receivingService.getById(id)
                ));
    }

    @GetMapping("/number/{receivingNumber}")
    @Operation(summary = "Get by receiving number")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    getByNumber(
            @PathVariable String receivingNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving fetched",
                        receivingService.getByReceivingNumber(
                                receivingNumber)
                ));
    }

    @GetMapping("/asn/{asnNumber}")
    @Operation(summary = "Get receiving by ASN")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    getByAsn(
            @PathVariable String asnNumber) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving fetched",
                        receivingService.getByAsnNumber(
                                asnNumber)
                ));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Start receiving")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    start(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving started",
                        receivingService.startReceiving(id)
                ));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Complete receiving")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    complete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving completed successfully",
                        receivingService.complete(id)
                ));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Cancel receiving")
    public ResponseEntity<ApiResponse<ReceivingResponse>>
    cancel(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Receiving cancelled",
                        receivingService.cancel(id)
                ));
    }
}
