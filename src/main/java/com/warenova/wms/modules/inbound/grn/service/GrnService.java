package com.warenova.wms.modules.inbound.grn.service;

import com.warenova.wms.modules.inbound.grn.dto.GrnRequest;
import com.warenova.wms.modules.inbound.grn.dto.GrnResponse;

import java.util.List;

public interface GrnService {

    // ── Create GRN ───────────────────────────────
    GrnResponse create(GrnRequest request);

    // ── Get by ID ────────────────────────────────
    GrnResponse getById(Long id);

    // ── Get by GRN number ────────────────────────
    GrnResponse getByGrnNumber(String grnNumber);

    // ── Get by ASN number ────────────────────────
    GrnResponse getByAsnNumber(String asnNumber);

    // ── Get all by warehouse ─────────────────────
    List<GrnResponse> getAllByWarehouse(
            String warehouseCode);

    // ── Confirm GRN ──────────────────────────────
    // Locks GRN, ready for inventory posting
    GrnResponse confirm(Long id);

    // ── Post GRN to inventory ────────────────────
    // Updates inventory stock levels
    GrnResponse post(Long id);

    // ── Cancel GRN ──────────────────────────────
    GrnResponse cancel(Long id);
}