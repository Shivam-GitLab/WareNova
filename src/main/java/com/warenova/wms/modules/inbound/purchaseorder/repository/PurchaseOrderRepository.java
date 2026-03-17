package com.warenova.wms.modules.inbound.purchaseorder.repository;

import com.warenova.wms.common.enums.POStatus;
import com.warenova.wms.modules.inbound.purchaseorder.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(
            String poNumber);

    boolean existsByPoNumber(String poNumber);

    List<PurchaseOrder> findByWarehouseCode(
            String warehouseCode);

    List<PurchaseOrder> findByWarehouseCodeAndStatus(
            String warehouseCode, POStatus status);

    List<PurchaseOrder> findBySupplierCode(
            String supplierCode);

    List<PurchaseOrder> findByWarehouseCodeAndSku(
            String warehouseCode, String sku);
}