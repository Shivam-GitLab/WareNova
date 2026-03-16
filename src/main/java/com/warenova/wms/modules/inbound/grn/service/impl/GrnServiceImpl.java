package com.warenova.wms.modules.inbound.grn.service.impl;

import com.warenova.wms.common.enums.GrnStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.grn.dto.GrnRequest;
import com.warenova.wms.modules.inbound.grn.dto.GrnResponse;
import com.warenova.wms.modules.inbound.grn.entity.Grn;
import com.warenova.wms.modules.inbound.grn.repository.GrnRepository;
import com.warenova.wms.modules.inbound.grn.service.GrnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrnServiceImpl implements GrnService {

    private final GrnRepository grnRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE GRN
    // ================================================
    @Override
    @Transactional
    public GrnResponse create(GrnRequest request) {

        log.info("Creating GRN for ASN: {}",
                request.getAsnNumber());

        // ── One GRN per ASN ───────────────────────
        if (grnRepository.existsByAsnNumber(
                request.getAsnNumber())) {
            throw new DuplicateResourceException(
                    "GRN", "ASN",
                    request.getAsnNumber()
            );
        }

        // ── Calculate quantities ──────────────────
        double damaged = request.getDamagedQuantity()
                != null ? request.getDamagedQuantity() : 0.0;
        double rejected = request.getRejectedQuantity()
                != null ? request.getRejectedQuantity() : 0.0;
        double accepted = request.getReceivedQuantity()
                - damaged - rejected;

        Grn grn = Grn.builder()
                .grnNumber(generateGrnNumber())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .asnNumber(request.getAsnNumber())
                .poNumber(request.getPoNumber())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .receivedDate(request.getReceivedDate())
                .expectedQuantity(
                        request.getExpectedQuantity())
                .receivedQuantity(
                        request.getReceivedQuantity())
                .damagedQuantity(damaged)
                .rejectedQuantity(rejected)
                .acceptedQuantity(accepted)
                .appointmentNumber(
                        request.getAppointmentNumber())
                .dockDoor(request.getDockDoor())
                .vehicleNumber(request.getVehicleNumber())
                .notes(request.getNotes())
                .status(GrnStatus.DRAFT)
                .build();

        Grn saved = grnRepository.save(grn);

        log.info("GRN created: {}", saved.getGrnNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public GrnResponse getById(Long id) {
        return mapToResponse(
                grnRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "GRN", String.valueOf(id)))
        );
    }

    // ================================================
    // GET BY GRN NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public GrnResponse getByGrnNumber(
            String grnNumber) {
        return mapToResponse(
                grnRepository.findByGrnNumber(grnNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "GRN", grnNumber))
        );
    }

    // ================================================
    // GET BY ASN NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public GrnResponse getByAsnNumber(
            String asnNumber) {
        return mapToResponse(
                grnRepository.findByAsnNumber(asnNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "GRN for ASN", asnNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<GrnResponse> getAllByWarehouse(
            String warehouseCode) {
        return grnRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // CONFIRM GRN
    // ================================================
    // Locks GRN — no more edits allowed
    // ================================================
    @Override
    @Transactional
    public GrnResponse confirm(Long id) {

        Grn grn = grnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "GRN", String.valueOf(id)));

        if (grn.getStatus() != GrnStatus.DRAFT) {
            throw new WMSException(
                    "Only DRAFT GRN can be confirmed"
            );
        }

        grn.setStatus(GrnStatus.CONFIRMED);
        log.info("GRN confirmed: {}", grn.getGrnNumber());
        return mapToResponse(grnRepository.save(grn));
    }

    // ================================================
    // POST GRN TO INVENTORY
    // ================================================
    // Updates stock levels
    // Called after GRN is confirmed
    // ================================================
    @Override
    @Transactional
    public GrnResponse post(Long id) {

        Grn grn = grnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "GRN", String.valueOf(id)));

        if (grn.getStatus() != GrnStatus.CONFIRMED) {
            throw new WMSException(
                    "Only CONFIRMED GRN can be posted"
            );
        }

        // ── TODO Phase 6 ──────────────────────────
        // Inventory update happens here
        // inventoryService.addStock(grn)

        grn.setStatus(GrnStatus.POSTED);
        log.info("GRN posted: {}", grn.getGrnNumber());
        return mapToResponse(grnRepository.save(grn));
    }

    // ================================================
    // CANCEL GRN
    // ================================================
    @Override
    @Transactional
    public GrnResponse cancel(Long id) {

        Grn grn = grnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "GRN", String.valueOf(id)));

        if (grn.getStatus() == GrnStatus.POSTED) {
            throw new WMSException(
                    "Posted GRN cannot be cancelled"
            );
        }

        grn.setStatus(GrnStatus.CANCELLED);
        log.info("GRN cancelled: {}",
                grn.getGrnNumber());
        return mapToResponse(grnRepository.save(grn));
    }

    // ================================================
    // GENERATE GRN NUMBER
    // ================================================
    private String generateGrnNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "GRN-" + timestamp;
    }

    // ================================================
    // MAP TO RESPONSE
    // ================================================
    private GrnResponse mapToResponse(Grn grn) {

        GrnResponse response =
                modelMapper.map(grn, GrnResponse.class);

        // ── Calculate variance ────────────────────
        double variance = grn.getExpectedQuantity()
                - grn.getReceivedQuantity();
        double variancePct = (variance /
                grn.getExpectedQuantity()) * 100;

        response.setVarianceQuantity(variance);
        response.setVariancePercentage(variancePct);
        response.setCreatedAt(grn.getCreatedAt());
        response.setCreatedBy(grn.getCreatedBy());
        response.setUpdatedAt(grn.getUpdatedAt());
        response.setUpdatedBy(grn.getUpdatedBy());

        return response;
    }
}