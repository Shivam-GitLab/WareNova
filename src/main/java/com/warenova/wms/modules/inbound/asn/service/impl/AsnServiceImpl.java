package com.warenova.wms.modules.inbound.asn.service.impl;

import com.warenova.wms.common.enums.ASNStatus;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.asn.dto.AsnRequest;
import com.warenova.wms.modules.inbound.asn.dto.AsnResponse;
import com.warenova.wms.modules.inbound.asn.entity.Asn;
import com.warenova.wms.modules.inbound.asn.repository.AsnRepository;
import com.warenova.wms.modules.inbound.asn.service.AsnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsnServiceImpl implements AsnService {

    private final AsnRepository asnRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE ASN
    // ================================================
    @Override
    @Transactional
    public AsnResponse create(AsnRequest request) {

        log.info("Creating ASN for PO: {}",
                request.getPoNumber());

        Asn asn = Asn.builder()
                .asnNumber(generateAsnNumber())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .poNumber(request.getPoNumber())
                .sku(request.getSku().toUpperCase())
                .asnQuantity(request.getAsnQuantity())
                .receivedQuantity(0.0)
                .vehicleNumber(request.getVehicleNumber())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .expectedDate(request.getExpectedDate())
                .appointmentNumber(
                        request.getAppointmentNumber())
                .dockDoor(request.getDockDoor())
                .palletCount(request.getPalletCount())
                .cartonCount(request.getCartonCount())
                .notes(request.getNotes())
                .status(ASNStatus.CREATED)
                .build();

        Asn saved = asnRepository.save(asn);
        log.info("ASN created: {}", saved.getAsnNumber());
        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public AsnResponse getById(Long id) {
        return mapToResponse(getAsnOrThrow(id));
    }

    // ================================================
    // GET BY ASN NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public AsnResponse getByAsnNumber(
            String asnNumber) {
        return mapToResponse(
                asnRepository.findByAsnNumber(asnNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "ASN", asnNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<AsnResponse> getAllByWarehouse(
            String warehouseCode) {
        return asnRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY PO NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<AsnResponse> getByPoNumber(
            String poNumber) {
        return asnRepository
                .findByPoNumber(poNumber)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY STATUS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<AsnResponse> getByStatus(
            String warehouseCode,
            String status) {
        return asnRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode,
                        ASNStatus.valueOf(status))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // MARK IN TRANSIT
    // ================================================
    // Truck left supplier warehouse
    // ================================================
    @Override
    @Transactional
    public AsnResponse markInTransit(Long id) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() != ASNStatus.CREATED) {
            throw new WMSException(
                    "Only CREATED ASN can be " +
                            "marked IN_TRANSIT"
            );
        }

        asn.setStatus(ASNStatus.IN_TRANSIT);
        log.info("ASN {} marked IN_TRANSIT",
                asn.getAsnNumber());
        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // MARK ARRIVED
    // ================================================
    // Truck arrived at warehouse dock
    // ================================================
    @Override
    @Transactional
    public AsnResponse markArrived(Long id) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() != ASNStatus.IN_TRANSIT) {
            throw new WMSException(
                    "Only IN_TRANSIT ASN can " +
                            "be marked ARRIVED"
            );
        }

        asn.setStatus(ASNStatus.ARRIVED);
        asn.setActualArrivalDate(LocalDate.now());

        log.info("ASN {} marked ARRIVED",
                asn.getAsnNumber());
        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // START RECEIVING
    // ================================================
    @Override
    @Transactional
    public AsnResponse startReceiving(Long id) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() != ASNStatus.ARRIVED) {
            throw new WMSException(
                    "Only ARRIVED ASN can " +
                            "start receiving"
            );
        }

        asn.setStatus(ASNStatus.RECEIVING);
        log.info("ASN {} receiving started",
                asn.getAsnNumber());
        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // CLOSE ASN
    // ================================================
    // Receiving complete
    // Triggers GRN generation
    // ================================================
    @Override
    @Transactional
    public AsnResponse close(Long id) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() != ASNStatus.RECEIVING) {
            throw new WMSException(
                    "Only RECEIVING ASN can be closed"
            );
        }

        asn.setStatus(ASNStatus.CLOSED);

        // ── TODO: Auto trigger GRN creation ──────
        // grnService.createFromAsn(asn)

        log.info("ASN {} closed → GRN to be generated",
                asn.getAsnNumber());
        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // CANCEL ASN
    // ================================================
    @Override
    @Transactional
    public AsnResponse cancel(Long id) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() == ASNStatus.CLOSED) {
            throw new WMSException(
                    "Closed ASN cannot be cancelled"
            );
        }

        asn.setStatus(ASNStatus.CANCELLED);
        log.info("ASN {} cancelled",
                asn.getAsnNumber());
        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // UPDATE ASN
    // ================================================
    @Override
    @Transactional
    public AsnResponse update(
            Long id, AsnRequest request) {

        Asn asn = getAsnOrThrow(id);

        if (asn.getStatus() != ASNStatus.CREATED) {
            throw new WMSException(
                    "Only CREATED ASN can be updated"
            );
        }

        asn.setAsnQuantity(request.getAsnQuantity());
        asn.setVehicleNumber(request.getVehicleNumber());
        asn.setDriverName(request.getDriverName());
        asn.setDriverPhone(request.getDriverPhone());
        asn.setExpectedDate(request.getExpectedDate());
        asn.setDockDoor(request.getDockDoor());
        asn.setPalletCount(request.getPalletCount());
        asn.setCartonCount(request.getCartonCount());
        asn.setNotes(request.getNotes());

        return mapToResponse(asnRepository.save(asn));
    }

    // ================================================
    // PRIVATE HELPERS
    // ================================================
    private Asn getAsnOrThrow(Long id) {
        return asnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "ASN", String.valueOf(id)));
    }

    private String generateAsnNumber() {
        String ts = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "ASN-" + ts;
    }

    private AsnResponse mapToResponse(Asn asn) {
        AsnResponse response =
                modelMapper.map(asn, AsnResponse.class);
        response.setCreatedAt(asn.getCreatedAt());
        response.setCreatedBy(asn.getCreatedBy());
        response.setUpdatedAt(asn.getUpdatedAt());
        response.setUpdatedBy(asn.getUpdatedBy());
        return response;
    }
}