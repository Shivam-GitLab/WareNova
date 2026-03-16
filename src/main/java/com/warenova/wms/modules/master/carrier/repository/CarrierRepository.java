package com.warenova.wms.modules.master.carrier.repository;

import com.warenova.wms.modules.master.carrier.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// CARRIER REPOSITORY
// ================================================

@Repository
public interface CarrierRepository
        extends JpaRepository<Carrier, Long> {

    // ── Find by carrier code ─────────────────────
    Optional<Carrier> findByCarrierCode(
            String carrierCode
    );

    // ── Check if exists ──────────────────────────
    boolean existsByCarrierCode(String carrierCode);

    // ── Find all by warehouse ────────────────────
    List<Carrier> findByWarehouseCode(
            String warehouseCode
    );

    // ── Find active carriers ─────────────────────
    List<Carrier> findByWarehouseCodeAndActiveTrue(
            String warehouseCode
    );

    // ── Find by carrier type ─────────────────────
    // Example: find all COURIER type carriers
    List<Carrier> findByWarehouseCodeAndCarrierType(
            String warehouseCode,
            String carrierType
    );
}