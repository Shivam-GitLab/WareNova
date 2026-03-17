package com.warenova.wms.modules.inbound.receiving.service;

import com.warenova.wms.modules.inbound.receiving.dto.ReceivingRequest;
import com.warenova.wms.modules.inbound.receiving.dto.ReceivingResponse;

import java.util.List;

public interface ReceivingService {

    // ── Create receiving ─────────────────────────
    ReceivingResponse create(ReceivingRequest request);

    // ── Get by ID ────────────────────────────────
    ReceivingResponse getById(Long id);

    // ── Get by receiving number ──────────────────
    ReceivingResponse getByReceivingNumber(
            String receivingNumber);

    // ── Get by ASN number ────────────────────────
    ReceivingResponse getByAsnNumber(String asnNumber);

    // ── Get all by warehouse ─────────────────────
    List<ReceivingResponse> getAllByWarehouse(
            String warehouseCode);

    // ── Get today's receivings ───────────────────
    List<ReceivingResponse> getTodaysReceivings(
            String warehouseCode);

    // ── Start receiving ──────────────────────────
    ReceivingResponse startReceiving(Long id);

    // ── Complete receiving ───────────────────────
    // Triggers ASN close + GRN creation
    ReceivingResponse complete(Long id);

    // ── Cancel receiving ─────────────────────────
    ReceivingResponse cancel(Long id);
}