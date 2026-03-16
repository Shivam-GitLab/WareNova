package com.warenova.wms.modules.master.customer.service;

import com.warenova.wms.modules.master.customer.dto.CustomerRequest;
import com.warenova.wms.modules.master.customer.dto.CustomerResponse;

import java.util.List;

// ================================================
// CUSTOMER SERVICE INTERFACE
// ================================================

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    CustomerResponse getById(Long id);

    CustomerResponse getByCode(String customerCode);

    List<CustomerResponse> getAllByWarehouse(
            String warehouseCode
    );

    List<CustomerResponse> getAllActiveByWarehouse(
            String warehouseCode
    );

    CustomerResponse update(
            Long id,
            CustomerRequest request
    );

    void deactivate(Long id);
}