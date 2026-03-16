package com.warenova.wms.modules.master.warehouse.service.impl;

import com.warenova.wms.common.enums.WarehouseStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.warehouse.dto.WarehouseRequest;
import com.warenova.wms.modules.master.warehouse.dto.WarehouseResponse;
import com.warenova.wms.modules.master.warehouse.entity.Warehouse;
import com.warenova.wms.modules.master.warehouse.repository.WarehouseRepository;
import com.warenova.wms.modules.master.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// WAREHOUSE SERVICE IMPLEMENTATION
// ================================================
// All business logic for warehouse operations
// Uses ModelMapper for entity ↔ DTO conversion
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE WAREHOUSE
    // ================================================
    // 1. Check code not already taken
    // 2. Map request DTO → entity
    // 3. Save to DB
    // 4. Map entity → response DTO
    // 5. Return response
    // ================================================
    @Override
    @Transactional
    public WarehouseResponse create(
            WarehouseRequest request) {

        log.info("Creating warehouse: {}",
                request.getWarehouseCode());

        // ── Check duplicate code ──────────────────
        if (warehouseRepository.existsByWarehouseCode(
                request.getWarehouseCode())) {
            throw new DuplicateResourceException(
                    "Warehouse",
                    "code",
                    request.getWarehouseCode()
            );
        }

        // ── Map DTO → Entity ──────────────────────
        Warehouse warehouse = Warehouse.builder()
                .warehouseCode(request.getWarehouseCode()
                        .toUpperCase())
                .warehouseName(request.getWarehouseName())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pincode(request.getPincode())
                .contactPerson(request.getContactPerson())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .totalAreaSqft(request.getTotalAreaSqft())
                .is3pl(request.getIs3pl())
                .status(WarehouseStatus.ACTIVE)
                .build();

        // ── Save to DB ────────────────────────────
        Warehouse saved =
                warehouseRepository.save(warehouse);

        log.info("Warehouse created: {}",
                saved.getWarehouseCode());

        // ── Map Entity → Response DTO ─────────────
        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getById(Long id) {

        Warehouse warehouse = warehouseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse", String.valueOf(id)
                        )
                );

        return mapToResponse(warehouse);
    }

    // ================================================
    // GET BY CODE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getByCode(
            String warehouseCode) {

        Warehouse warehouse = warehouseRepository
                .findByWarehouseCode(warehouseCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse", warehouseCode
                        )
                );

        return mapToResponse(warehouse);
    }

    // ================================================
    // GET ALL WAREHOUSES
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAll() {

        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET ALL ACTIVE WAREHOUSES
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAllActive() {

        return warehouseRepository
                .findByStatus(WarehouseStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE WAREHOUSE
    // ================================================
    // Cannot change warehouseCode after creation
    // Only update other fields
    // ================================================
    @Override
    @Transactional
    public WarehouseResponse update(
            Long id,
            WarehouseRequest request) {

        log.info("Updating warehouse id: {}", id);

        // ── Find existing warehouse ───────────────
        Warehouse warehouse = warehouseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse", String.valueOf(id)
                        )
                );

        // ── Update fields ─────────────────────────
        // Note: warehouseCode NOT updated
        // Code is permanent identifier
        warehouse.setWarehouseName(
                request.getWarehouseName());
        warehouse.setAddressLine1(
                request.getAddressLine1());
        warehouse.setAddressLine2(
                request.getAddressLine2());
        warehouse.setCity(request.getCity());
        warehouse.setState(request.getState());
        warehouse.setCountry(request.getCountry());
        warehouse.setPincode(request.getPincode());
        warehouse.setContactPerson(
                request.getContactPerson());
        warehouse.setContactPhone(
                request.getContactPhone());
        warehouse.setContactEmail(
                request.getContactEmail());
        warehouse.setTotalAreaSqft(
                request.getTotalAreaSqft());
        warehouse.setIs3pl(request.getIs3pl());

        // ── Save updated entity ───────────────────
        Warehouse updated =
                warehouseRepository.save(warehouse);

        log.info("Warehouse updated: {}",
                updated.getWarehouseCode());

        return mapToResponse(updated);
    }

    // ================================================
    // DEACTIVATE WAREHOUSE (SOFT DELETE)
    // ================================================
    // Never hard delete in WMS!
    // Just set status = INACTIVE
    // All historical data preserved
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        log.info("Deactivating warehouse id: {}", id);

        Warehouse warehouse = warehouseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse", String.valueOf(id)
                        )
                );

        // ── Set inactive ──────────────────────────
        warehouse.setStatus(WarehouseStatus.INACTIVE);
        warehouseRepository.save(warehouse);

        log.info("Warehouse deactivated: {}",
                warehouse.getWarehouseCode());
    }

    // ================================================
// ACTIVATE WAREHOUSE
// ================================================
    @Override
    @Transactional
    public void activate(Long id) {

        Warehouse warehouse = warehouseRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse", String.valueOf(id)
                        )
                );

        warehouse.setStatus(WarehouseStatus.ACTIVE);
        warehouseRepository.save(warehouse);

        log.info("Warehouse activated: {}",
                warehouse.getWarehouseCode());
    }
    // ================================================
    // MAP ENTITY TO RESPONSE DTO (PRIVATE HELPER)
    // ================================================
    // Converts Warehouse entity → WarehouseResponse
    // Used by all methods above
    // ModelMapper copies matching field names
    // ================================================
    private WarehouseResponse mapToResponse(
            Warehouse warehouse) {

        WarehouseResponse response =
                modelMapper.map(
                        warehouse,
                        WarehouseResponse.class
                );

        // ── Manually map audit fields ─────────────
        // BaseEntity fields need manual mapping
        response.setCreatedAt(warehouse.getCreatedAt());
        response.setCreatedBy(warehouse.getCreatedBy());
        response.setUpdatedAt(warehouse.getUpdatedAt());
        response.setUpdatedBy(warehouse.getUpdatedBy());

        return response;
    }
}