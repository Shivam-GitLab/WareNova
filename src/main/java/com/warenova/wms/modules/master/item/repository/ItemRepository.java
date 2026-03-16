package com.warenova.wms.modules.master.item.repository;

import com.warenova.wms.common.enums.ItemStatus;
import com.warenova.wms.modules.master.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// ITEM REPOSITORY
// ================================================

@Repository
public interface ItemRepository
        extends JpaRepository<Item, Long> {

    // ── Find by SKU ──────────────────────────────
    Optional<Item> findBySku(String sku);

    // ── Check SKU exists ─────────────────────────
    boolean existsBySku(String sku);

    // ── Find by warehouse ────────────────────────
    List<Item> findByWarehouseCode(
            String warehouseCode
    );

    // ── Find by status ───────────────────────────
    List<Item> findByWarehouseCodeAndStatus(
            String warehouseCode,
            ItemStatus status
    );

    // ── Find by category ─────────────────────────
    List<Item> findByWarehouseCodeAndCategory(
            String warehouseCode,
            String category
    );

    // ── Search by name or SKU ────────────────────
    // Used for search box in frontend
    @Query("SELECT i FROM Item i WHERE " +
            "i.warehouseCode = :warehouseCode AND " +
            "(LOWER(i.itemName) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
            "LOWER(i.sku) LIKE LOWER(CONCAT('%',:search,'%')))")
    List<Item> searchItems(
            @Param("warehouseCode") String warehouseCode,
            @Param("search") String search
    );

    // ── Find low stock items ─────────────────────
    // Items below reorder level
    @Query("SELECT i FROM Item i WHERE " +
            "i.warehouseCode = :warehouseCode AND " +
            "i.reorderLevel IS NOT NULL AND " +
            "i.status = 'ACTIVE'")
    List<Item> findLowStockItems(
            @Param("warehouseCode") String warehouseCode
    );
}