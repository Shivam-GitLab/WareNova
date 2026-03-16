package com.warenova.wms.modules.master.location.service;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.modules.master.location.dto.LocationRequest;
import com.warenova.wms.modules.master.location.dto.LocationResponse;

import java.util.List;

// ================================================
// LOCATION SERVICE INTERFACE
// ================================================

public interface LocationService {

    // ── Create location ──────────────────────────
    LocationResponse create(LocationRequest request);

    // ── Get by ID ────────────────────────────────
    LocationResponse getById(Long id);

    // ── Get by code and warehouse ────────────────
    LocationResponse getByCode(
            String locationCode,
            String warehouseCode
    );

    // ── Get all by warehouse ─────────────────────
    List<LocationResponse> getAllByWarehouse(
            String warehouseCode
    );

    // ── Get by status ────────────────────────────
    List<LocationResponse> getByStatus(
            String warehouseCode,
            LocationStatus status
    );

    // ── Update location ──────────────────────────
    LocationResponse update(
            Long id,
            LocationRequest request
    );

    // ── Update location status ───────────────────
    // Called by putaway and picking automatically
    void updateStatus(Long id, LocationStatus status);

    // ── Deactivate ───────────────────────────────
    void deactivate(Long id);
}