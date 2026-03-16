package com.warenova.wms.modules.master.supplier.service;

import com.warenova.wms.modules.master.supplier.dto.SupplierRequest;
import com.warenova.wms.modules.master.supplier.dto.SupplierResponse;

import java.util.List;

// ================================================
// SUPPLIER SERVICE INTERFACE
// ================================================

public interface SupplierService {

    SupplierResponse create(SupplierRequest request);

    SupplierResponse getById(Long id);

    SupplierResponse getByCode(String supplierCode);

    List<SupplierResponse> getAllByWarehouse(
            String warehouseCode
    );

    List<SupplierResponse> getAllActiveByWarehouse(
            String warehouseCode
    );

    SupplierResponse update(
            Long id,
            SupplierRequest request
    );

    void deactivate(Long id);
}