package com.warenova.wms.modules.master.location.service.impl;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.location.dto.LocationRequest;
import com.warenova.wms.modules.master.location.dto.LocationResponse;
import com.warenova.wms.modules.master.location.entity.Location;
import com.warenova.wms.modules.master.location.repository.LocationRepository;
import com.warenova.wms.modules.master.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// LOCATION SERVICE IMPLEMENTATION
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl
        implements LocationService {

    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE LOCATION
    // ================================================
    @Override
    @Transactional
    public LocationResponse create(
            LocationRequest request) {

        log.info("Creating location: {} in warehouse: {}",
                request.getLocationCode(),
                request.getWarehouseCode());

        // ── Check duplicate ───────────────────────
        if (locationRepository
                .existsByLocationCodeAndWarehouseCode(
                        request.getLocationCode(),
                        request.getWarehouseCode())) {
            throw new DuplicateResourceException(
                    "Location",
                    "code",
                    request.getLocationCode()
            );
        }

        // ── Build entity ──────────────────────────
        Location location = Location.builder()
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .locationCode(
                        request.getLocationCode().toUpperCase())
                .zone(request.getZone())
                .aisle(request.getAisle())
                .bay(request.getBay())
                .level(request.getLevel())
                .locationType(request.getLocationType())
                .maxWeightKg(request.getMaxWeightKg())
                .maxVolumeCbm(request.getMaxVolumeCbm())
                .allowMixedSku(request.getAllowMixedSku())
                // New location always starts EMPTY
                .status(LocationStatus.EMPTY)
                .active(true)
                .build();

        Location saved =
                locationRepository.save(location);

        log.info("Location created: {}",
                saved.getLocationCode());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public LocationResponse getById(Long id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location", String.valueOf(id)
                        )
                );
        return mapToResponse(location);
    }

    // ================================================
    // GET BY CODE AND WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public LocationResponse getByCode(
            String locationCode,
            String warehouseCode) {

        Location location = locationRepository
                .findByLocationCodeAndWarehouseCode(
                        locationCode, warehouseCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location", locationCode
                        )
                );
        return mapToResponse(location);
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getAllByWarehouse(
            String warehouseCode) {

        return locationRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY STATUS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getByStatus(
            String warehouseCode,
            LocationStatus status) {

        return locationRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode, status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE LOCATION
    // ================================================
    @Override
    @Transactional
    public LocationResponse update(
            Long id,
            LocationRequest request) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location", String.valueOf(id)
                        )
                );

        // ── Update fields ─────────────────────────
        // locationCode and warehouseCode NOT updated
        location.setZone(request.getZone());
        location.setAisle(request.getAisle());
        location.setBay(request.getBay());
        location.setLevel(request.getLevel());
        location.setLocationType(
                request.getLocationType());
        location.setMaxWeightKg(
                request.getMaxWeightKg());
        location.setMaxVolumeCbm(
                request.getMaxVolumeCbm());
        location.setAllowMixedSku(
                request.getAllowMixedSku());

        Location updated =
                locationRepository.save(location);

        return mapToResponse(updated);
    }

    // ================================================
    // UPDATE STATUS
    // ================================================
    // Called by Putaway → EMPTY → OCCUPIED
    // Called by Picking → OCCUPIED → EMPTY
    // ================================================
    @Override
    @Transactional
    public void updateStatus(
            Long id,
            LocationStatus status) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location", String.valueOf(id)
                        )
                );

        location.setStatus(status);
        locationRepository.save(location);

        log.info("Location {} status updated to: {}",
                location.getLocationCode(), status);
    }

    // ================================================
    // DEACTIVATE
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        Location location = locationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location", String.valueOf(id)
                        )
                );

        location.setActive(false);
        locationRepository.save(location);

        log.info("Location deactivated: {}",
                location.getLocationCode());
    }

    // ================================================
    // MAP TO RESPONSE (PRIVATE HELPER)
    // ================================================
    private LocationResponse mapToResponse(
            Location location) {

        LocationResponse response =
                modelMapper.map(
                        location,
                        LocationResponse.class
                );

        response.setCreatedAt(location.getCreatedAt());
        response.setCreatedBy(location.getCreatedBy());
        response.setUpdatedAt(location.getUpdatedAt());
        response.setUpdatedBy(location.getUpdatedBy());

        return response;
    }
}