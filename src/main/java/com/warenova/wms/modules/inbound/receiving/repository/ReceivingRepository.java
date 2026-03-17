package com.warenova.wms.modules.inbound.receiving.repository;

import com.warenova.wms.common.enums.ReceivingStatus;
import com.warenova.wms.modules.inbound.receiving.entity.Receiving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivingRepository
        extends JpaRepository<Receiving, Long> {

    Optional<Receiving> findByReceivingNumber(
            String receivingNumber);

    boolean existsByReceivingNumber(
            String receivingNumber);

    Optional<Receiving> findByAsnNumber(
            String asnNumber);

    List<Receiving> findByWarehouseCode(
            String warehouseCode);

    List<Receiving> findByWarehouseCodeAndStatus(
            String warehouseCode,
            ReceivingStatus status);

    List<Receiving> findBySupplierCode(
            String supplierCode);

    List<Receiving> findByPoNumber(String poNumber);

    // ── Today's receivings ────────────────────────
    @Query("SELECT r FROM Receiving r WHERE " +
            "r.warehouseCode = :warehouseCode AND " +
            "r.receivedDateTime BETWEEN " +
            ":start AND :end")
    List<Receiving> findTodaysReceivings(
            @Param("warehouseCode") String warehouseCode,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}