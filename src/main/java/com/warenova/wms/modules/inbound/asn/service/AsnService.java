package com.warenova.wms.modules.inbound.asn.service;

import com.warenova.wms.modules.inbound.asn.dto.AsnRequest;
import com.warenova.wms.modules.inbound.asn.dto.AsnResponse;

import java.util.List;

public interface AsnService {

    // ── Create ASN ───────────────────────────────
    AsnResponse create(AsnRequest request);

    // ── Get by ID ────────────────────────────────
    AsnResponse getById(Long id);

    // ── Get by ASN number ────────────────────────
    AsnResponse getByAsnNumber(String asnNumber);

    // ── Get all by warehouse ─────────────────────
    List<AsnResponse> getAllByWarehouse(
            String warehouseCode);

    // ── Get by PO number ─────────────────────────
    List<AsnResponse> getByPoNumber(String poNumber);

    // ── Get by status ────────────────────────────
    List<AsnResponse> getByStatus(
            String warehouseCode, String status);

    // ── Mark in transit ──────────────────────────
    // Truck left supplier
    AsnResponse markInTransit(Long id);

    // ── Mark arrived ─────────────────────────────
    // Truck at dock
    AsnResponse markArrived(Long id);

    // ── Start receiving ──────────────────────────
    AsnResponse startReceiving(Long id);

    // ── Close ASN ────────────────────────────────
    // Receiving complete → triggers GRN
    AsnResponse close(Long id);

    // ── Cancel ASN ───────────────────────────────
    AsnResponse cancel(Long id);

    // ── Update ASN ───────────────────────────────
    AsnResponse update(Long id, AsnRequest request);
}