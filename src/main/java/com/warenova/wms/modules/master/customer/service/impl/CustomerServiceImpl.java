package com.warenova.wms.modules.master.customer.service.impl;

import com.warenova.wms.common.enums.CustomerStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.customer.dto.CustomerRequest;
import com.warenova.wms.modules.master.customer.dto.CustomerResponse;
import com.warenova.wms.modules.master.customer.entity.Customer;
import com.warenova.wms.modules.master.customer.repository.CustomerRepository;
import com.warenova.wms.modules.master.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// CUSTOMER SERVICE IMPLEMENTATION
// ================================================
// NOTE:
// Package is service.impl (MNC standard)
// Separates interface from implementation
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl
        implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE CUSTOMER
    // ================================================
    @Override
    @Transactional
    public CustomerResponse create(
            CustomerRequest request) {

        log.info("Creating customer: {}",
                request.getCustomerCode());

        // ── Check duplicate ───────────────────────
        if (customerRepository.existsByCustomerCode(
                request.getCustomerCode())) {
            throw new DuplicateResourceException(
                    "Customer",
                    "code",
                    request.getCustomerCode()
            );
        }

        // ── Build entity ──────────────────────────
        Customer customer = Customer.builder()
                .customerCode(
                        request.getCustomerCode().toUpperCase())
                .customerName(request.getCustomerName())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pincode(request.getPincode())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .creditLimit(request.getCreditLimit())
                .paymentTermsDays(
                        request.getPaymentTermsDays())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .status(CustomerStatus.ACTIVE)
                .build();

        Customer saved =
                customerRepository.save(customer);

        log.info("Customer created: {}",
                saved.getCustomerCode());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {

        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer", String.valueOf(id)
                        )
                );
        return mapToResponse(customer);
    }

    // ================================================
    // GET BY CODE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getByCode(
            String customerCode) {

        Customer customer = customerRepository
                .findByCustomerCode(customerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer", customerCode
                        )
                );
        return mapToResponse(customer);
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllByWarehouse(
            String warehouseCode) {

        return customerRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET ALL ACTIVE BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllActiveByWarehouse(
            String warehouseCode) {

        return customerRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode,
                        CustomerStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE CUSTOMER
    // ================================================
    @Override
    @Transactional
    public CustomerResponse update(
            Long id,
            CustomerRequest request) {

        log.info("Updating customer id: {}", id);

        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer", String.valueOf(id)
                        )
                );

        // ── customerCode NOT updated ──────────────
        customer.setCustomerName(
                request.getCustomerName());
        customer.setContactPerson(
                request.getContactPerson());
        customer.setContactEmail(
                request.getContactEmail());
        customer.setContactPhone(
                request.getContactPhone());
        customer.setAddressLine1(
                request.getAddressLine1());
        customer.setAddressLine2(
                request.getAddressLine2());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setCountry(request.getCountry());
        customer.setPincode(request.getPincode());
        customer.setGstNumber(request.getGstNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setCreditLimit(
                request.getCreditLimit());
        customer.setPaymentTermsDays(
                request.getPaymentTermsDays());

        Customer updated =
                customerRepository.save(customer);

        log.info("Customer updated: {}",
                updated.getCustomerCode());

        return mapToResponse(updated);
    }

    // ================================================
    // DEACTIVATE CUSTOMER
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer", String.valueOf(id)
                        )
                );

        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);

        log.info("Customer deactivated: {}",
                customer.getCustomerCode());
    }

    // ================================================
    // MAP TO RESPONSE (PRIVATE HELPER)
    // ================================================
    private CustomerResponse mapToResponse(
            Customer customer) {

        CustomerResponse response =
                modelMapper.map(
                        customer,
                        CustomerResponse.class
                );

        response.setCreatedAt(customer.getCreatedAt());
        response.setCreatedBy(customer.getCreatedBy());
        response.setUpdatedAt(customer.getUpdatedAt());
        response.setUpdatedBy(customer.getUpdatedBy());

        return response;
    }
}