package com.warenova.wms.modules.inbound.grn.repository;

import com.warenova.wms.common.enums.GrnStatus;
import com.warenova.wms.modules.inbound.grn.entity.Grn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrnRepository
        extends JpaRepository<Grn, Long> {

    Optional<Grn> findByGrnNumber(String grnNumber);

    boolean existsByGrnNumber(String grnNumber);

    boolean existsByAsnNumber(String asnNumber);

    List<Grn> findByWarehouseCode(String warehouseCode);

    List<Grn> findByWarehouseCodeAndStatus(
            String warehouseCode, GrnStatus status);

    List<Grn> findBySupplierCode(String supplierCode);

    Optional<Grn> findByAsnNumber(String asnNumber);

    // ── GRNs in date range ────────────────────────
    @Query("SELECT g FROM Grn g WHERE " +
            "g.warehouseCode = :warehouseCode AND " +
            "g.receivedDate BETWEEN :start AND :end")
    List<Grn> findByDateRange(
            @Param("warehouseCode") String warehouseCode,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}