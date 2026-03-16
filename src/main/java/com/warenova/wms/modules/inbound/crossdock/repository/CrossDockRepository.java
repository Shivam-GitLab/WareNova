package com.warenova.wms.modules.inbound.crossdock.repository;

import com.warenova.wms.common.enums.CrossDockStatus;
import com.warenova.wms.modules.inbound.crossdock.entity.CrossDock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrossDockRepository
        extends JpaRepository<CrossDock, Long> {

    Optional<CrossDock> findByCrossdockNumber(
            String crossdockNumber);

    boolean existsByCrossdockNumber(
            String crossdockNumber);

    List<CrossDock> findByWarehouseCode(
            String warehouseCode);

    List<CrossDock> findByWarehouseCodeAndStatus(
            String warehouseCode, CrossDockStatus status);

    List<CrossDock> findByAsnNumber(String asnNumber);

    List<CrossDock> findBySalesOrderNumber(
            String salesOrderNumber);

    List<CrossDock> findByCustomerCode(
            String customerCode);
}