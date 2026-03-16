package com.warenova.wms.modules.master.lpn.repository;

import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.modules.master.lpn.entity.Lpn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LpnRepository
        extends JpaRepository<Lpn, Long> {

    Optional<Lpn> findByLpnNumber(String lpnNumber);

    boolean existsByLpnNumber(String lpnNumber);

    List<Lpn> findByWarehouseCode(String warehouseCode);

    List<Lpn> findByWarehouseCodeAndStatus(
            String warehouseCode, LpnStatus status);

    List<Lpn> findByCurrentLocation(
            String currentLocation);

    List<Lpn> findByAsnNumber(String asnNumber);

    List<Lpn> findByParentLpnNumber(
            String parentLpnNumber);
}