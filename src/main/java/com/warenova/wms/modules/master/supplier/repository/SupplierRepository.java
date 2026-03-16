package com.warenova.wms.modules.master.supplier.repository;

import com.warenova.wms.common.enums.SupplierStatus;
import com.warenova.wms.modules.master.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// SUPPLIER REPOSITORY
// ================================================

@Repository
public interface SupplierRepository
        extends JpaRepository<Supplier, Long> {

    // ── Find by supplier code ────────────────────
    Optional<Supplier> findBySupplierCode(
            String supplierCode
    );

    // ── Check if exists ──────────────────────────
    boolean existsBySupplierCode(String supplierCode);

    // ── Find all by warehouse ────────────────────
    List<Supplier> findByWarehouseCode(
            String warehouseCode
    );

    // ── Find by status ───────────────────────────
    List<Supplier> findByWarehouseCodeAndStatus(
            String warehouseCode,
            SupplierStatus status
    );

    // ── Find active suppliers ────────────────────
    List<Supplier> findByWarehouseCodeAndStatusNot(
            String warehouseCode,
            SupplierStatus status
    );
}