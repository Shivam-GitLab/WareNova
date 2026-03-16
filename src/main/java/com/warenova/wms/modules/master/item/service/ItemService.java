package com.warenova.wms.modules.master.item.service;

import com.warenova.wms.modules.master.item.dto.ItemRequest;
import com.warenova.wms.modules.master.item.dto.ItemResponse;

import java.util.List;

// ================================================
// ITEM SERVICE INTERFACE
// ================================================

public interface ItemService {

    // ── Create item ──────────────────────────────
    ItemResponse create(ItemRequest request);

    // ── Get by ID ────────────────────────────────
    ItemResponse getById(Long id);

    // ── Get by SKU ───────────────────────────────
    ItemResponse getBySku(String sku);

    // ── Get all by warehouse ─────────────────────
    List<ItemResponse> getAllByWarehouse(
            String warehouseCode
    );

    // ── Search items ─────────────────────────────
    List<ItemResponse> searchItems(
            String warehouseCode,
            String search
    );

    // ── Update item ──────────────────────────────
    ItemResponse update(Long id, ItemRequest request);

    // ── Deactivate item ──────────────────────────
    void deactivate(Long id);
}