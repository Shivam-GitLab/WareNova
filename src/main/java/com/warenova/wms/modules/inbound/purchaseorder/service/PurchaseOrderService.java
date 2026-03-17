package com.warenova.wms.modules.inbound.purchaseorder.service;

import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderRequest;
import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {

    PurchaseOrderResponse create(
            PurchaseOrderRequest request);

    PurchaseOrderResponse getById(Long id);

    PurchaseOrderResponse getByPoNumber(
            String poNumber);

    List<PurchaseOrderResponse> getAllByWarehouse(
            String warehouseCode);

    List<PurchaseOrderResponse> getByStatus(
            String warehouseCode, String status);

    // ── Confirm PO → sends to supplier ───────────
    PurchaseOrderResponse confirm(Long id);

    // ── Close PO → all received ──────────────────
    PurchaseOrderResponse close(Long id);

    // ── Cancel PO ────────────────────────────────
    PurchaseOrderResponse cancel(Long id);

    PurchaseOrderResponse update(
            Long id, PurchaseOrderRequest request);
}