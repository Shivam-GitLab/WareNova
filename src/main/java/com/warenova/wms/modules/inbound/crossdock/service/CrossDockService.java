package com.warenova.wms.modules.inbound.crossdock.service;

import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockRequest;
import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockResponse;

import java.util.List;

public interface CrossDockService {

    CrossDockResponse create(CrossDockRequest request);

    CrossDockResponse getById(Long id);

    CrossDockResponse getByNumber(
            String crossdockNumber);

    List<CrossDockResponse> getAllByWarehouse(
            String warehouseCode);

    List<CrossDockResponse> getByAsn(String asnNumber);

    // ── Process cross dock ───────────────────────
    // Move from receiving to dispatch location
    CrossDockResponse process(Long id);

    // ── Complete cross dock ──────────────────────
    CrossDockResponse complete(Long id);

    // ── Cancel cross dock ────────────────────────
    CrossDockResponse cancel(Long id);
}