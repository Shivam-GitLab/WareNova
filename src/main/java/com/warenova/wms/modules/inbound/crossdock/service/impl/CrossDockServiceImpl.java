package com.warenova.wms.modules.inbound.crossdock.service.impl;

import com.warenova.wms.common.enums.CrossDockStatus;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockRequest;
import com.warenova.wms.modules.inbound.crossdock.dto.CrossDockResponse;
import com.warenova.wms.modules.inbound.crossdock.entity.CrossDock;
import com.warenova.wms.modules.inbound.crossdock.repository.CrossDockRepository;
import com.warenova.wms.modules.inbound.crossdock.service.CrossDockService;
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
public class CrossDockServiceImpl
        implements CrossDockService {

    private final CrossDockRepository crossDockRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE CROSS DOCK
    // ================================================
    @Override
    @Transactional
    public CrossDockResponse create(
            CrossDockRequest request) {

        log.info("Creating cross dock for SKU: {}",
                request.getSku());

        CrossDock crossDock = CrossDock.builder()
                .crossdockNumber(generateCdNumber())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .asnNumber(request.getAsnNumber())
                .poNumber(request.getPoNumber())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .inboundLpn(request.getInboundLpn())
                .receivingLocation(
                        request.getReceivingLocation())
                .sku(request.getSku().toUpperCase())
                .quantity(request.getQuantity())
                .salesOrderNumber(
                        request.getSalesOrderNumber())
                .customerCode(
                        request.getCustomerCode().toUpperCase())
                .dispatchLocation(
                        request.getDispatchLocation())
                .outboundLpn(request.getOutboundLpn())
                .carrierCode(request.getCarrierCode())
                .status(CrossDockStatus.PENDING)
                .build();

        CrossDock saved =
                crossDockRepository.save(crossDock);

        log.info("Cross dock created: {}",
                saved.getCrossdockNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CrossDockResponse getById(Long id) {
        return mapToResponse(
                crossDockRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "CrossDock", String.valueOf(id)))
        );
    }

    // ================================================
    // GET BY NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public CrossDockResponse getByNumber(
            String crossdockNumber) {
        return mapToResponse(
                crossDockRepository
                        .findByCrossdockNumber(crossdockNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "CrossDock", crossdockNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<CrossDockResponse> getAllByWarehouse(
            String warehouseCode) {
        return crossDockRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY ASN
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<CrossDockResponse> getByAsn(
            String asnNumber) {
        return crossDockRepository
                .findByAsnNumber(asnNumber)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // PROCESS CROSS DOCK
    // ================================================
    // Moves goods from receiving → dispatch
    // ================================================
    @Override
    @Transactional
    public CrossDockResponse process(Long id) {

        CrossDock cd = crossDockRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CrossDock", String.valueOf(id)));

        if (cd.getStatus() != CrossDockStatus.PENDING) {
            throw new WMSException(
                    "Only PENDING cross dock can be processed"
            );
        }

        cd.setStatus(CrossDockStatus.IN_PROGRESS);

        log.info("Cross dock processing: {}",
                cd.getCrossdockNumber());

        return mapToResponse(
                crossDockRepository.save(cd));
    }

    // ================================================
    // COMPLETE CROSS DOCK
    // ================================================
    @Override
    @Transactional
    public CrossDockResponse complete(Long id) {

        CrossDock cd = crossDockRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CrossDock", String.valueOf(id)));

        if (cd.getStatus() !=
                CrossDockStatus.IN_PROGRESS) {
            throw new WMSException(
                    "Only IN_PROGRESS cross dock " +
                            "can be completed"
            );
        }

        cd.setStatus(CrossDockStatus.COMPLETED);

        log.info("Cross dock completed: {}",
                cd.getCrossdockNumber());

        return mapToResponse(
                crossDockRepository.save(cd));
    }

    // ================================================
    // CANCEL CROSS DOCK
    // ================================================
    @Override
    @Transactional
    public CrossDockResponse cancel(Long id) {

        CrossDock cd = crossDockRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CrossDock", String.valueOf(id)));

        if (cd.getStatus() ==
                CrossDockStatus.COMPLETED) {
            throw new WMSException(
                    "Completed cross dock cannot be cancelled"
            );
        }

        cd.setStatus(CrossDockStatus.CANCELLED);

        log.info("Cross dock cancelled: {}",
                cd.getCrossdockNumber());

        return mapToResponse(
                crossDockRepository.save(cd));
    }

    // ================================================
    // GENERATE CROSS DOCK NUMBER
    // ================================================
    private String generateCdNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "CD-" + timestamp;
    }

    // ================================================
    // MAP TO RESPONSE
    // ================================================
    private CrossDockResponse mapToResponse(
            CrossDock cd) {
        CrossDockResponse response =
                modelMapper.map(cd,
                        CrossDockResponse.class);
        response.setCreatedAt(cd.getCreatedAt());
        response.setCreatedBy(cd.getCreatedBy());
        response.setUpdatedAt(cd.getUpdatedAt());
        response.setUpdatedBy(cd.getUpdatedBy());
        return response;
    }
}