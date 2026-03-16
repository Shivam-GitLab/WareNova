package com.warenova.wms.modules.master.customer.repository;

import com.warenova.wms.common.enums.CustomerStatus;
import com.warenova.wms.modules.master.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ================================================
// CUSTOMER REPOSITORY
// ================================================

@Repository
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    // ── Find by customer code ────────────────────
    Optional<Customer> findByCustomerCode(
            String customerCode
    );

    // ── Check if exists ──────────────────────────
    boolean existsByCustomerCode(String customerCode);

    // ── Find all by warehouse ────────────────────
    List<Customer> findByWarehouseCode(
            String warehouseCode
    );

    // ── Find by status ───────────────────────────
    List<Customer> findByWarehouseCodeAndStatus(
            String warehouseCode,
            CustomerStatus status
    );
}