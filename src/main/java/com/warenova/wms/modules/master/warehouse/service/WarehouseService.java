package com.warenova.wms.modules.master.warehouse.service;

import com.warenova.wms.modules.master.warehouse.dto.WarehouseRequest;
import com.warenova.wms.modules.master.warehouse.dto.WarehouseResponse;

import java.util.List;

// ================================================
// WAREHOUSE SERVICE INTERFACE
// ================================================
// Defines contract for warehouse operations
// Impl class provides actual logic
// ================================================

public interface WarehouseService {

    // ── Create new warehouse ─────────────────────
    WarehouseResponse create(WarehouseRequest request);

    // ── Get warehouse by ID ──────────────────────
    WarehouseResponse getById(Long id);

    // ── Get warehouse by code ────────────────────
    WarehouseResponse getByCode(String warehouseCode);

    // ── Get all warehouses ───────────────────────
    List<WarehouseResponse> getAll();

    // ── Get all active warehouses ────────────────
    List<WarehouseResponse> getAllActive();

    // ── Update warehouse ─────────────────────────
    WarehouseResponse update(
            Long id,
            WarehouseRequest request
    );

    // ── Deactivate warehouse (soft delete) ───────
    void deactivate(Long id);

    // ── Activate warehouse ───────
    void activate(Long id);
}