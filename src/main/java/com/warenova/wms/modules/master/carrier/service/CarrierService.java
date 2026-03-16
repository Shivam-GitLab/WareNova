package com.warenova.wms.modules.master.carrier.service;

import com.warenova.wms.modules.master.carrier.dto.CarrierRequest;
import com.warenova.wms.modules.master.carrier.dto.CarrierResponse;

import java.util.List;

// ================================================
// CARRIER SERVICE INTERFACE
// ================================================

public interface CarrierService {

    // ── Create carrier ───────────────────────────
    CarrierResponse create(CarrierRequest request);

    // ── Get by ID ────────────────────────────────
    CarrierResponse getById(Long id);

    // ── Get by code ──────────────────────────────
    CarrierResponse getByCode(String carrierCode);

    // ── Get all by warehouse ─────────────────────
    List<CarrierResponse> getAllByWarehouse(
            String warehouseCode
    );

    // ── Get active by warehouse ──────────────────
    List<CarrierResponse> getAllActiveByWarehouse(
            String warehouseCode
    );

    // ── Update carrier ───────────────────────────
    CarrierResponse update(
            Long id,
            CarrierRequest request
    );

    // ── Deactivate carrier ───────────────────────
    void deactivate(Long id);
}