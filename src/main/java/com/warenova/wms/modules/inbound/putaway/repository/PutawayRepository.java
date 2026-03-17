package com.warenova.wms.modules.inbound.putaway.repository;

import com.warenova.wms.common.enums.PutawayStatus;
import com.warenova.wms.modules.inbound.putaway.entity.Putaway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// PUTAWAY REPOSITORY
// ================================================

@Repository
public interface PutawayRepository
        extends JpaRepository<Putaway, Long> {

    // ── Find by putaway number ───────────────────
    Optional<Putaway> findByPutawayNumber(
            String putawayNumber);

    // ── Find by warehouse ────────────────────────
    List<Putaway> findByWarehouseCode(
            String warehouseCode);

    // ── Find by status ───────────────────────────
    List<Putaway> findByWarehouseCodeAndStatus(
            String warehouseCode,
            PutawayStatus status);

    // ── Find by receiving number ─────────────────
    List<Putaway> findByReceivingNumber(
            String receivingNumber);

    // ── Find by LPN ──────────────────────────────
    Optional<Putaway> findByLpnNumber(
            String lpnNumber);

    // ── Find pending tasks for staff ─────────────
    List<Putaway> findByWarehouseCodeAndAssignedToAndStatus(
            String warehouseCode,
            String assignedTo,
            PutawayStatus status);

    // ── Find by GRN ──────────────────────────────
    List<Putaway> findByGrnNumber(String grnNumber);

    // ── Count pending by warehouse ───────────────
    @Query("SELECT COUNT(p) FROM Putaway p WHERE " +
            "p.warehouseCode = :warehouseCode AND " +
            "p.status = 'PENDING'")
    Long countPendingByWarehouse(
            @Param("warehouseCode") String warehouseCode);
}