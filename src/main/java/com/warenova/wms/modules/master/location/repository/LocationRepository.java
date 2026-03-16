package com.warenova.wms.modules.master.location.repository;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.enums.LocationType;
import com.warenova.wms.modules.master.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// LOCATION REPOSITORY
// ================================================

@Repository
public interface LocationRepository
        extends JpaRepository<Location, Long> {

    // ── Find by code and warehouse ───────────────
    // Location code is unique per warehouse
    Optional<Location> findByLocationCodeAndWarehouseCode(
            String locationCode,
            String warehouseCode
    );

    // ── Check if exists ──────────────────────────
    boolean existsByLocationCodeAndWarehouseCode(
            String locationCode,
            String warehouseCode
    );

    // ── Find all locations in warehouse ──────────
    List<Location> findByWarehouseCode(
            String warehouseCode
    );

    // ── Find empty locations ─────────────────────
    // Used by putaway to find available locations
    List<Location> findByWarehouseCodeAndStatus(
            String warehouseCode,
            LocationStatus status
    );

    // ── Find by type ─────────────────────────────
    // Example: find all DOCK locations
    List<Location> findByWarehouseCodeAndLocationType(
            String warehouseCode,
            LocationType locationType
    );

    // ── Find empty storage locations ─────────────
    // Most common putaway query
    List<Location> findByWarehouseCodeAndStatusAndLocationType(
            String warehouseCode,
            LocationStatus status,
            LocationType locationType
    );

    // ── Find by zone ─────────────────────────────
    List<Location> findByWarehouseCodeAndZone(
            String warehouseCode,
            String zone
    );
}