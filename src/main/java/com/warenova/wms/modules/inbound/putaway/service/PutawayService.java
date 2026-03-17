package com.warenova.wms.modules.inbound.putaway.service;

import com.warenova.wms.modules.inbound.putaway.dto.PutawayRequest;
import com.warenova.wms.modules.inbound.putaway.dto.PutawayResponse;

import java.util.List;

// ================================================
// PUTAWAY SERVICE INTERFACE
// ================================================

public interface PutawayService {

    // ── Create putaway task ──────────────────────
    PutawayResponse create(PutawayRequest request);

    // ── Get by ID ────────────────────────────────
    PutawayResponse getById(Long id);

    // ── Get by putaway number ────────────────────
    PutawayResponse getByPutawayNumber(
            String putawayNumber);

    // ── Get all by warehouse ─────────────────────
    List<PutawayResponse> getAllByWarehouse(
            String warehouseCode);

    // ── Get pending tasks ────────────────────────
    List<PutawayResponse> getPendingTasks(
            String warehouseCode);

    // ── Get by receiving number ──────────────────
    List<PutawayResponse> getByReceivingNumber(
            String receivingNumber);

    // ── Get tasks for staff ──────────────────────
    List<PutawayResponse> getTasksForStaff(
            String warehouseCode,
            String assignedTo);

    // ── Suggest location ─────────────────────────
    // System suggests best bin for item
    String suggestLocation(
            String warehouseCode,
            String sku);

    // ── Start putaway ────────────────────────────
    PutawayResponse startPutaway(Long id);

    // ── Complete putaway ─────────────────────────
    // Updates LPN location + Location status
    PutawayResponse completePutaway(
            Long id, String toLocation);

    // ── Cancel putaway ───────────────────────────
    PutawayResponse cancel(Long id);
}