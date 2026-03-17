package com.warenova.wms.modules.inbound.asn.repository;

import com.warenova.wms.common.enums.ASNStatus;
import com.warenova.wms.modules.inbound.asn.entity.Asn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsnRepository
        extends JpaRepository<Asn, Long> {

    Optional<Asn> findByAsnNumber(String asnNumber);

    boolean existsByAsnNumber(String asnNumber);

    List<Asn> findByWarehouseCode(String warehouseCode);

    List<Asn> findByWarehouseCodeAndStatus(
            String warehouseCode, ASNStatus status);

    List<Asn> findByPoNumber(String poNumber);

    List<Asn> findBySupplierCode(String supplierCode);

    Optional<Asn> findByAppointmentNumber(
            String appointmentNumber);
}