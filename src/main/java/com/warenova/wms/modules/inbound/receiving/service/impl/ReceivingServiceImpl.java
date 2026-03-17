package com.warenova.wms.modules.inbound.receiving.service.impl;

import com.warenova.wms.common.enums.ReceivingStatus;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.receiving.dto.ReceivingRequest;
import com.warenova.wms.modules.inbound.receiving.dto.ReceivingResponse;
import com.warenova.wms.modules.inbound.receiving.entity.Receiving;
import com.warenova.wms.modules.inbound.receiving.repository.ReceivingRepository;
import com.warenova.wms.modules.inbound.receiving.service.ReceivingService;
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
public class ReceivingServiceImpl
        implements ReceivingService {

    private final ReceivingRepository
            receivingRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE RECEIVING
    // ================================================
    @Override
    @Transactional
    public ReceivingResponse create(
            ReceivingRequest request) {

        log.info("Creating receiving for ASN: {}",
                request.getAsnNumber());

        // ── Calculate quantities ──────────────────
        double damaged = request.getDamagedQuantity()
                != null
                ? request.getDamagedQuantity() : 0.0;

        double rejected = request.getRejectedQuantity()
                != null
                ? request.getRejectedQuantity() : 0.0;

        double accepted = request.getReceivedQuantity()
                - damaged - rejected;

        double shortage = request.getExpectedQuantity()
                - request.getReceivedQuantity();

        // ── Generate IGP number ───────────────────
        String igpNumber = generateIgpNumber();

        Receiving receiving = Receiving.builder()
                .receivingNumber(generateRcvNumber())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .asnNumber(request.getAsnNumber())
                .poNumber(request.getPoNumber())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .sku(request.getSku().toUpperCase())
                .expectedQuantity(
                        request.getExpectedQuantity())
                .receivedQuantity(
                        request.getReceivedQuantity())
                .acceptedQuantity(accepted)
                .damagedQuantity(damaged)
                .rejectedQuantity(rejected)
                .shortageQuantity(
                        shortage > 0 ? shortage : 0.0)
                .dockDoor(request.getDockDoor())
                .stagingLocation(
                        request.getStagingLocation())
                .lpnNumber(request.getLpnNumber())
                .vehicleNumber(request.getVehicleNumber())
                .receivedDateTime(
                        request.getReceivedDateTime())
                .igpNumber(igpNumber)
                .notes(request.getNotes())
                .status(ReceivingStatus.PENDING)
                .build();

        Receiving saved =
                receivingRepository.save(receiving);

        log.info("Receiving created: {} IGP: {}",
                saved.getReceivingNumber(),
                saved.getIgpNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public ReceivingResponse getById(Long id) {
        return mapToResponse(getOrThrow(id));
    }

    // ================================================
    // GET BY RECEIVING NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public ReceivingResponse getByReceivingNumber(
            String receivingNumber) {
        return mapToResponse(
                receivingRepository
                        .findByReceivingNumber(receivingNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receiving", receivingNumber))
        );
    }

    // ================================================
    // GET BY ASN NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public ReceivingResponse getByAsnNumber(
            String asnNumber) {
        return mapToResponse(
                receivingRepository
                        .findByAsnNumber(asnNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receiving for ASN", asnNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReceivingResponse> getAllByWarehouse(
            String warehouseCode) {
        return receivingRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET TODAY'S RECEIVINGS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<ReceivingResponse> getTodaysReceivings(
            String warehouseCode) {

        LocalDateTime start =
                LocalDateTime.now()
                        .toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        return receivingRepository
                .findTodaysReceivings(
                        warehouseCode, start, end)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // START RECEIVING
    // ================================================
    @Override
    @Transactional
    public ReceivingResponse startReceiving(Long id) {

        Receiving receiving = getOrThrow(id);

        if (receiving.getStatus() !=
                ReceivingStatus.PENDING) {
            throw new WMSException(
                    "Only PENDING receiving can be started"
            );
        }

        receiving.setStatus(
                ReceivingStatus.IN_PROGRESS);

        log.info("Receiving started: {}",
                receiving.getReceivingNumber());

        return mapToResponse(
                receivingRepository.save(receiving));
    }

    // ================================================
    // COMPLETE RECEIVING
    // ================================================
    // Triggers:
    // → ASN status → CLOSED
    // → GRN creation (Phase 5 complete)
    // → Putaway tasks created
    // ================================================
    @Override
    @Transactional
    public ReceivingResponse complete(Long id) {

        Receiving receiving = getOrThrow(id);

        if (receiving.getStatus() !=
                ReceivingStatus.IN_PROGRESS) {
            throw new WMSException(
                    "Only IN_PROGRESS receiving " +
                            "can be completed"
            );
        }

        receiving.setStatus(ReceivingStatus.COMPLETED);

        // ── TODO: Chain triggers ──────────────────
        // asnService.close(asnNumber)
        // grnService.createFromReceiving(receiving)
        // putawayService.createTask(receiving)

        log.info("Receiving completed: {}",
                receiving.getReceivingNumber());

        return mapToResponse(
                receivingRepository.save(receiving));
    }

    // ================================================
    // CANCEL RECEIVING
    // ================================================
    @Override
    @Transactional
    public ReceivingResponse cancel(Long id) {

        Receiving receiving = getOrThrow(id);

        if (receiving.getStatus() ==
                ReceivingStatus.COMPLETED) {
            throw new WMSException(
                    "Completed receiving cannot be cancelled"
            );
        }

        receiving.setStatus(ReceivingStatus.CANCELLED);

        log.info("Receiving cancelled: {}",
                receiving.getReceivingNumber());

        return mapToResponse(
                receivingRepository.save(receiving));
    }

    // ================================================
    // PRIVATE HELPERS
    // ================================================
    private Receiving getOrThrow(Long id) {
        return receivingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiving", String.valueOf(id)));
    }

    private String generateRcvNumber() {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "RCV-" + ts;
    }

    private String generateIgpNumber() {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "IGP-" + ts;
    }

    private ReceivingResponse mapToResponse(
            Receiving r) {
        ReceivingResponse response =
                modelMapper.map(r, ReceivingResponse.class);
        response.setCreatedAt(r.getCreatedAt());
        response.setCreatedBy(r.getCreatedBy());
        response.setUpdatedAt(r.getUpdatedAt());
        response.setUpdatedBy(r.getUpdatedBy());
        return response;
    }
}