package com.warenova.wms.modules.master.carrier.service.impl;

import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.carrier.dto.CarrierRequest;
import com.warenova.wms.modules.master.carrier.dto.CarrierResponse;
import com.warenova.wms.modules.master.carrier.entity.Carrier;
import com.warenova.wms.modules.master.carrier.repository.CarrierRepository;
import com.warenova.wms.modules.master.carrier.service.CarrierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// CARRIER SERVICE IMPLEMENTATION
// ================================================
// Package: service.impl (MNC standard)
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class CarrierServiceImpl
        implements CarrierService {

    private final CarrierRepository carrierRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE CARRIER
    // ================================================
    @Override
    @Transactional
    public CarrierResponse create(
            CarrierRequest request) {

        log.info("Creating carrier: {}",
                request.getCarrierCode());

        // ── Check duplicate ───────────────────────
        if (carrierRepository.existsByCarrierCode(
                request.getCarrierCode())) {
            throw new DuplicateResourceException(
                    "Carrier",
                    "code",
                    request.getCarrierCode()
            );
        }

        // ── Build entity ──────────────────────────
        Carrier carrier = Carrier.builder()
                .carrierCode(
                        request.getCarrierCode().toUpperCase())
                .carrierName(request.getCarrierName())
                .carrierType(request.getCarrierType())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .trackingUrl(request.getTrackingUrl())
                .accountNumber(request.getAccountNumber())
                .serviceTypes(request.getServiceTypes())
                .maxWeightKg(request.getMaxWeightKg())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .active(true)
                .build();

        Carrier saved = carrierRepository.save(carrier);

        log.info("Carrier created: {}",
                saved.getCarrierCode());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CarrierResponse getById(Long id) {

        Carrier carrier = carrierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Carrier", String.valueOf(id)
                        )
                );
        return mapToResponse(carrier);
    }

    // ================================================
    // GET BY CODE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CarrierResponse getByCode(
            String carrierCode) {

        Carrier carrier = carrierRepository
                .findByCarrierCode(carrierCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Carrier", carrierCode
                        )
                );
        return mapToResponse(carrier);
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<CarrierResponse> getAllByWarehouse(
            String warehouseCode) {

        return carrierRepository
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
    public List<CarrierResponse> getAllActiveByWarehouse(
            String warehouseCode) {

        return carrierRepository
                .findByWarehouseCodeAndActiveTrue(
                        warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE CARRIER
    // ================================================
    @Override
    @Transactional
    public CarrierResponse update(
            Long id,
            CarrierRequest request) {

        log.info("Updating carrier id: {}", id);

        Carrier carrier = carrierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Carrier", String.valueOf(id)
                        )
                );

        // ── carrierCode NOT updated ───────────────
        carrier.setCarrierName(request.getCarrierName());
        carrier.setCarrierType(request.getCarrierType());
        carrier.setContactPerson(
                request.getContactPerson());
        carrier.setContactEmail(
                request.getContactEmail());
        carrier.setContactPhone(
                request.getContactPhone());
        carrier.setTrackingUrl(request.getTrackingUrl());
        carrier.setAccountNumber(
                request.getAccountNumber());
        carrier.setServiceTypes(
                request.getServiceTypes());
        carrier.setMaxWeightKg(request.getMaxWeightKg());

        Carrier updated = carrierRepository.save(carrier);

        log.info("Carrier updated: {}",
                updated.getCarrierCode());

        return mapToResponse(updated);
    }

    // ================================================
    // DEACTIVATE CARRIER
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        Carrier carrier = carrierRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Carrier", String.valueOf(id)
                        )
                );

        carrier.setActive(false);
        carrierRepository.save(carrier);

        log.info("Carrier deactivated: {}",
                carrier.getCarrierCode());
    }

    // ================================================
    // MAP TO RESPONSE (PRIVATE HELPER)
    // ================================================
    private CarrierResponse mapToResponse(
            Carrier carrier) {

        CarrierResponse response =
                modelMapper.map(
                        carrier,
                        CarrierResponse.class
                );

        response.setCreatedAt(carrier.getCreatedAt());
        response.setCreatedBy(carrier.getCreatedBy());
        response.setUpdatedAt(carrier.getUpdatedAt());
        response.setUpdatedBy(carrier.getUpdatedBy());

        return response;
    }
}