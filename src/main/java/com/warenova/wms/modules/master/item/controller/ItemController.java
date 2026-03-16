package com.warenova.wms.modules.master.item.controller;

import com.warenova.wms.common.response.ApiResponse;
import com.warenova.wms.modules.master.item.dto.ItemRequest;
import com.warenova.wms.modules.master.item.dto.ItemResponse;
import com.warenova.wms.modules.master.item.service.ItemService;
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
// ITEM CONTROLLER
// ================================================
// BASE URL: /api/master/items
// ================================================

@RestController
@RequestMapping("/api/master/items")
@RequiredArgsConstructor
@Tag(
        name = "Item Master",
        description = "Item/SKU management APIs"
)
public class ItemController {

    private final ItemService itemService;

    // ── Create item ──────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Create new item/SKU")
    public ResponseEntity<ApiResponse<ItemResponse>>
    create(
            @Valid @RequestBody ItemRequest request) {

        ItemResponse response =
                itemService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Item created successfully",
                        response
                ));
    }

    // ── Get all by warehouse ─────────────────────
    @GetMapping("/warehouse/{warehouseCode}")
    @Operation(summary = "Get all items by warehouse")
    public ResponseEntity<ApiResponse<List<ItemResponse>>>
    getAllByWarehouse(
            @PathVariable String warehouseCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Items fetched successfully",
                        itemService.getAllByWarehouse(
                                warehouseCode)
                )
        );
    }

    // ── Get by ID ────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID")
    public ResponseEntity<ApiResponse<ItemResponse>>
    getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item fetched successfully",
                        itemService.getById(id)
                )
        );
    }

    // ── Get by SKU ───────────────────────────────
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get item by SKU")
    public ResponseEntity<ApiResponse<ItemResponse>>
    getBySku(@PathVariable String sku) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item fetched successfully",
                        itemService.getBySku(sku)
                )
        );
    }

    // ── Search items ─────────────────────────────
    @GetMapping("/search")
    @Operation(summary = "Search items by name or SKU")
    public ResponseEntity<ApiResponse<List<ItemResponse>>>
    search(
            @RequestParam String warehouseCode,
            @RequestParam String query) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Search results",
                        itemService.searchItems(
                                warehouseCode, query)
                )
        );
    }

    // ── Update item ──────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(" +
            "'ROLE_ADMIN','ROLE_SUPERVISOR')")
    @Operation(summary = "Update item")
    public ResponseEntity<ApiResponse<ItemResponse>>
    update(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item updated successfully",
                        itemService.update(id, request)
                )
        );
    }

    // ── Deactivate item ──────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Deactivate item")
    public ResponseEntity<ApiResponse<Void>>
    deactivate(@PathVariable Long id) {

        itemService.deactivate(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item deactivated successfully"
                )
        );
    }
}
