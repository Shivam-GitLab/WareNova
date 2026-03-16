package com.warenova.wms.modules.master.lpn.service;

import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.modules.master.lpn.dto.LpnRequest;
import com.warenova.wms.modules.master.lpn.dto.LpnResponse;

import java.util.List;

public interface LpnService {

    LpnResponse create(LpnRequest request);

    LpnResponse getById(Long id);

    LpnResponse getByLpnNumber(String lpnNumber);

    List<LpnResponse> getAllByWarehouse(
            String warehouseCode);

    List<LpnResponse> getByStatus(
            String warehouseCode, LpnStatus status);

    List<LpnResponse> getByLocation(
            String locationCode);

    // ── Move LPN to new location ─────────────────
    // Core operation — called by putaway/picking
    LpnResponse moveLpn(
            String lpnNumber,
            String newLocation
    );

    // ── Update LPN status ────────────────────────
    LpnResponse updateStatus(
            String lpnNumber,
            LpnStatus status
    );

    void cancel(Long id);
}