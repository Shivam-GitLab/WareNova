package com.warenova.wms.modules.inbound.putaway.service.impl;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.common.enums.PutawayStatus;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.putaway.dto.PutawayRequest;
import com.warenova.wms.modules.inbound.putaway.dto.PutawayResponse;
import com.warenova.wms.modules.inbound.putaway.entity.Putaway;
import com.warenova.wms.modules.inbound.putaway.repository.PutawayRepository;
import com.warenova.wms.modules.inbound.putaway.service.PutawayService;
import com.warenova.wms.modules.master.location.repository.LocationRepository;
import com.warenova.wms.modules.master.lpn.repository.LpnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

// ================================================
// PUTAWAY SERVICE IMPLEMENTATION
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class PutawayServiceImpl
        implements PutawayService {

    private final PutawayRepository putawayRepository;
    private final LocationRepository locationRepository;
    private final LpnRepository lpnRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE PUTAWAY TASK
    // ================================================
    @Override
    @Transactional
    public PutawayResponse create(
            PutawayRequest request) {

        log.info("Creating putaway task for LPN: {}",
                request.getLpnNumber());

        // ── Auto suggest location if not provided ─
        String suggested = request.getSuggestedLocation();
        if (suggested == null || suggested.isEmpty()) {
            suggested = suggestLocation(
                    request.getWarehouseCode(),
                    request.getSku()
            );
        }

        Putaway putaway = Putaway.builder()
                .putawayNumber(generatePutawayNumber())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .receivingNumber(
                        request.getReceivingNumber())
                .grnNumber(request.getGrnNumber())
                .lpnNumber(
                        request.getLpnNumber().toUpperCase())
                .sku(request.getSku().toUpperCase())
                .quantity(request.getQuantity())
                .fromLocation(
                        request.getFromLocation().toUpperCase())
                .suggestedLocation(
                        suggested != null
                                ? suggested.toUpperCase() : null)
                .toLocation(
                        request.getToLocation() != null
                                ? request.getToLocation()
                                .toUpperCase() : null)
                .putawayType(
                        request.getPutawayType() != null
                                ? request.getPutawayType()
                                : "ASSISTED")
                .assignedTo(request.getAssignedTo())
                .notes(request.getNotes())
                .status(PutawayStatus.PENDING)
                .build();

        Putaway saved = putawayRepository.save(putaway);

        log.info("Putaway task created: {}",
                saved.getPutawayNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public PutawayResponse getById(Long id) {
        return mapToResponse(getOrThrow(id));
    }

    // ================================================
    // GET BY PUTAWAY NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public PutawayResponse getByPutawayNumber(
            String putawayNumber) {
        return mapToResponse(
                putawayRepository
                        .findByPutawayNumber(putawayNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Putaway", putawayNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<PutawayResponse> getAllByWarehouse(
            String warehouseCode) {
        return putawayRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET PENDING TASKS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<PutawayResponse> getPendingTasks(
            String warehouseCode) {
        return putawayRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode,
                        PutawayStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY RECEIVING NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<PutawayResponse> getByReceivingNumber(
            String receivingNumber) {
        return putawayRepository
                .findByReceivingNumber(receivingNumber)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET TASKS FOR STAFF
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<PutawayResponse> getTasksForStaff(
            String warehouseCode,
            String assignedTo) {
        return putawayRepository
                .findByWarehouseCodeAndAssignedToAndStatus(
                        warehouseCode,
                        assignedTo,
                        PutawayStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // SUGGEST LOCATION
    // ================================================
    // Finds best available EMPTY STORAGE location
    // Real WMS uses slotting rules here
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public String suggestLocation(
            String warehouseCode,
            String sku) {

        // ── Find first available empty storage ────
        return locationRepository
                .findByWarehouseCodeAndStatusAndLocationType(
                        warehouseCode,
                        LocationStatus.EMPTY,
                        com.warenova.wms.common.enums
                                .LocationType.STORAGE)
                .stream()
                .findFirst()
                .map(location ->
                        location.getLocationCode())
                .orElse(null);
    }

    // ================================================
    // START PUTAWAY
    // ================================================
    // Staff picks up LPN and starts moving
    // ================================================
    @Override
    @Transactional
    public PutawayResponse startPutaway(Long id) {

        Putaway putaway = getOrThrow(id);

        if (putaway.getStatus() !=
                PutawayStatus.PENDING) {
            throw new WMSException(
                    "Only PENDING putaway can be started"
            );
        }

        // ── Update LPN status to IN_TRANSIT ───────
        lpnRepository
                .findByLpnNumber(putaway.getLpnNumber())
                .ifPresent(lpn -> {
                    lpn.setStatus(LpnStatus.IN_TRANSIT);
                    lpnRepository.save(lpn);
                });

        putaway.setStatus(PutawayStatus.IN_PROGRESS);

        log.info("Putaway started: {}",
                putaway.getPutawayNumber());

        return mapToResponse(
                putawayRepository.save(putaway));
    }

    // ================================================
    // COMPLETE PUTAWAY
    // ================================================
    // Staff scans destination location
    // Triggers:
    // → LPN location updated
    // → LPN status → CLOSED
    // → From location → EMPTY
    // → To location → OCCUPIED
    // ================================================
    @Override
    @Transactional
    public PutawayResponse completePutaway(
            Long id,
            String toLocation) {

        Putaway putaway = getOrThrow(id);

        if (putaway.getStatus() !=
                PutawayStatus.IN_PROGRESS) {
            throw new WMSException(
                    "Only IN_PROGRESS putaway " +
                            "can be completed"
            );
        }

        // ── Set actual to location ────────────────
        putaway.setToLocation(
                toLocation.toUpperCase());
        putaway.setStatus(PutawayStatus.COMPLETED);

        // ── Update LPN → moved to bin location ────
        lpnRepository
                .findByLpnNumber(putaway.getLpnNumber())
                .ifPresent(lpn -> {
                    lpn.setCurrentLocation(
                            toLocation.toUpperCase());
                    lpn.setStatus(LpnStatus.CLOSED);
                    lpnRepository.save(lpn);
                });

        // ── Update FROM location → EMPTY ──────────
        locationRepository
                .findByLocationCodeAndWarehouseCode(
                        putaway.getFromLocation(),
                        putaway.getWarehouseCode())
                .ifPresent(loc -> {
                    loc.setStatus(LocationStatus.EMPTY);
                    locationRepository.save(loc);
                });

        // ── Update TO location → OCCUPIED ─────────
        locationRepository
                .findByLocationCodeAndWarehouseCode(
                        toLocation.toUpperCase(),
                        putaway.getWarehouseCode())
                .ifPresent(loc -> {
                    loc.setStatus(LocationStatus.OCCUPIED);
                    locationRepository.save(loc);
                });

        // ── TODO Phase 6 ──────────────────────────
        // inventoryService.addStock(
        //   warehouseCode, sku,
        //   quantity, toLocation)

        log.info("Putaway completed: {} → {}",
                putaway.getPutawayNumber(), toLocation);

        return mapToResponse(
                putawayRepository.save(putaway));
    }

    // ================================================
    // CANCEL PUTAWAY
    // ================================================
    @Override
    @Transactional
    public PutawayResponse cancel(Long id) {

        Putaway putaway = getOrThrow(id);

        if (putaway.getStatus() ==
                PutawayStatus.COMPLETED) {
            throw new WMSException(
                    "Completed putaway cannot be cancelled"
            );
        }

        // ── Reset LPN status if IN_TRANSIT ────────
        if (putaway.getStatus() ==
                PutawayStatus.IN_PROGRESS) {
            lpnRepository
                    .findByLpnNumber(
                            putaway.getLpnNumber())
                    .ifPresent(lpn -> {
                        lpn.setStatus(LpnStatus.ACTIVE);
                        lpnRepository.save(lpn);
                    });
        }

        putaway.setStatus(PutawayStatus.CANCELLED);

        log.info("Putaway cancelled: {}",
                putaway.getPutawayNumber());

        return mapToResponse(
                putawayRepository.save(putaway));
    }

    // ================================================
    // PRIVATE HELPERS
    // ================================================
    private Putaway getOrThrow(Long id) {
        return putawayRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Putaway", String.valueOf(id)));
    }

    private String generatePutawayNumber() {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "PUT-" + ts;
    }

    private PutawayResponse mapToResponse(
            Putaway p) {
        PutawayResponse response =
                modelMapper.map(p, PutawayResponse.class);
        response.setCreatedAt(p.getCreatedAt());
        response.setCreatedBy(p.getCreatedBy());
        response.setUpdatedAt(p.getUpdatedAt());
        response.setUpdatedBy(p.getUpdatedBy());
        return response;
    }
}