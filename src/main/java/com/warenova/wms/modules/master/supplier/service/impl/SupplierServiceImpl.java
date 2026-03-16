package com.warenova.wms.modules.master.supplier.service.impl;

import com.warenova.wms.common.enums.SupplierStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.supplier.dto.SupplierRequest;
import com.warenova.wms.modules.master.supplier.dto.SupplierResponse;
import com.warenova.wms.modules.master.supplier.entity.Supplier;
import com.warenova.wms.modules.master.supplier.repository.SupplierRepository;
import com.warenova.wms.modules.master.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// SUPPLIER SERVICE IMPLEMENTATION
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl
        implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE SUPPLIER
    // ================================================
    @Override
    @Transactional
    public SupplierResponse create(
            SupplierRequest request) {

        log.info("Creating supplier: {}",
                request.getSupplierCode());

        // ── Check duplicate code ──────────────────
        if (supplierRepository.existsBySupplierCode(
                request.getSupplierCode())) {
            throw new DuplicateResourceException(
                    "Supplier",
                    "code",
                    request.getSupplierCode()
            );
        }

        // ── Build entity ──────────────────────────
        Supplier supplier = Supplier.builder()
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .supplierName(request.getSupplierName())
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
                .paymentTermsDays(
                        request.getPaymentTermsDays())
                .leadTimeDays(request.getLeadTimeDays())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .status(SupplierStatus.ACTIVE)
                .build();

        Supplier saved =
                supplierRepository.save(supplier);

        log.info("Supplier created: {}",
                saved.getSupplierCode());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getById(Long id) {

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier", String.valueOf(id)
                        )
                );
        return mapToResponse(supplier);
    }

    // ================================================
    // GET BY CODE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getByCode(
            String supplierCode) {

        Supplier supplier = supplierRepository
                .findBySupplierCode(supplierCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier", supplierCode
                        )
                );
        return mapToResponse(supplier);
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllByWarehouse(
            String warehouseCode) {

        return supplierRepository
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
    public List<SupplierResponse> getAllActiveByWarehouse(
            String warehouseCode) {

        return supplierRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode,
                        SupplierStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE SUPPLIER
    // ================================================
    @Override
    @Transactional
    public SupplierResponse update(
            Long id,
            SupplierRequest request) {

        log.info("Updating supplier id: {}", id);

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier", String.valueOf(id)
                        )
                );

        // ── supplierCode NOT updated ──────────────
        supplier.setSupplierName(
                request.getSupplierName());
        supplier.setContactPerson(
                request.getContactPerson());
        supplier.setContactEmail(
                request.getContactEmail());
        supplier.setContactPhone(
                request.getContactPhone());
        supplier.setAddressLine1(
                request.getAddressLine1());
        supplier.setAddressLine2(
                request.getAddressLine2());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setCountry(request.getCountry());
        supplier.setPincode(request.getPincode());
        supplier.setGstNumber(request.getGstNumber());
        supplier.setPanNumber(request.getPanNumber());
        supplier.setPaymentTermsDays(
                request.getPaymentTermsDays());
        supplier.setLeadTimeDays(
                request.getLeadTimeDays());

        Supplier updated =
                supplierRepository.save(supplier);

        log.info("Supplier updated: {}",
                updated.getSupplierCode());

        return mapToResponse(updated);
    }

    // ================================================
    // DEACTIVATE SUPPLIER
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        Supplier supplier = supplierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Supplier", String.valueOf(id)
                        )
                );

        supplier.setStatus(SupplierStatus.INACTIVE);
        supplierRepository.save(supplier);

        log.info("Supplier deactivated: {}",
                supplier.getSupplierCode());
    }

    // ================================================
    // MAP TO RESPONSE (PRIVATE HELPER)
    // ================================================
    private SupplierResponse mapToResponse(
            Supplier supplier) {

        SupplierResponse response =
                modelMapper.map(
                        supplier,
                        SupplierResponse.class
                );

        response.setCreatedAt(supplier.getCreatedAt());
        response.setCreatedBy(supplier.getCreatedBy());
        response.setUpdatedAt(supplier.getUpdatedAt());
        response.setUpdatedBy(supplier.getUpdatedBy());

        return response;
    }
}