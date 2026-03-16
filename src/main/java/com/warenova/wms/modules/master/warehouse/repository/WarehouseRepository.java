package com.warenova.wms.modules.master.warehouse.repository;

import com.warenova.wms.common.enums.WarehouseStatus;
import com.warenova.wms.modules.master.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// WAREHOUSE REPOSITORY
// ================================================
// All DB operations for Warehouse entity
// JpaRepository gives free CRUD methods
// We add custom queries below
// ================================================

@Repository
public interface WarehouseRepository
        extends JpaRepository<Warehouse, Long> {

    // ── Find by warehouse code ───────────────────
    // Used in all transaction validations
    Optional<Warehouse> findByWarehouseCode(
            String warehouseCode
    );

    // ── Check if code exists ─────────────────────
    // Used before creating new warehouse
    boolean existsByWarehouseCode(String warehouseCode);

    // ── Find all by status ───────────────────────
    // Used to get all ACTIVE warehouses
    List<Warehouse> findByStatus(WarehouseStatus status);

    // ── Find active warehouses only ──────────────
    // Shortcut for findByStatus(ACTIVE)
    List<Warehouse> findByStatusAndIs3pl(
            WarehouseStatus status,
            Boolean is3pl
    );
}