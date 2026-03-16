package com.warenova.wms.modules.master.lpn.service.impl;

import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.lpn.dto.LpnRequest;
import com.warenova.wms.modules.master.lpn.dto.LpnResponse;
import com.warenova.wms.modules.master.lpn.entity.Lpn;
import com.warenova.wms.modules.master.lpn.repository.LpnRepository;
import com.warenova.wms.modules.master.lpn.service.LpnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LpnServiceImpl implements LpnService {

    private final LpnRepository lpnRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE LPN
    // ================================================
    @Override
    @Transactional
    public LpnResponse create(LpnRequest request) {

        log.info("Creating LPN: {}",
                request.getLpnNumber());

        if (lpnRepository.existsByLpnNumber(
                request.getLpnNumber())) {
            throw new DuplicateResourceException(
                    "LPN", "number",
                    request.getLpnNumber()
            );
        }

        Lpn lpn = Lpn.builder()
                .lpnNumber(
                        request.getLpnNumber().toUpperCase())
                .lpnType(request.getLpnType())
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .currentLocation(
                        request.getCurrentLocation())
                .parentLpnNumber(
                        request.getParentLpnNumber())
                .asnNumber(request.getAsnNumber())
                .poNumber(request.getPoNumber())
                .grossWeightKg(request.getGrossWeightKg())
                .lengthCm(request.getLengthCm())
                .widthCm(request.getWidthCm())
                .heightCm(request.getHeightCm())
                .status(LpnStatus.EMPTY)
                .build();

        Lpn saved = lpnRepository.save(lpn);
        log.info("LPN created: {}", saved.getLpnNumber());
        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public LpnResponse getById(Long id) {
        return mapToResponse(
                lpnRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "LPN", String.valueOf(id)))
        );
    }

    // ================================================
    // GET BY LPN NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public LpnResponse getByLpnNumber(
            String lpnNumber) {
        return mapToResponse(
                lpnRepository.findByLpnNumber(lpnNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "LPN", lpnNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<LpnResponse> getAllByWarehouse(
            String warehouseCode) {
        return lpnRepository
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
    public List<LpnResponse> getByStatus(
            String warehouseCode,
            LpnStatus status) {
        return lpnRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode, status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // GET BY LOCATION
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<LpnResponse> getByLocation(
            String locationCode) {
        return lpnRepository
                .findByCurrentLocation(locationCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // MOVE LPN
    // ================================================
    // Core WMS operation
    // Called by Putaway → moves to bin
    // Called by Picking → moves to staging
    // ================================================
    @Override
    @Transactional
    public LpnResponse moveLpn(
            String lpnNumber,
            String newLocation) {

        log.info("Moving LPN {} to {}",
                lpnNumber, newLocation);

        Lpn lpn = lpnRepository
                .findByLpnNumber(lpnNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "LPN", lpnNumber));

        String oldLocation = lpn.getCurrentLocation();
        lpn.setCurrentLocation(newLocation);
        lpn.setStatus(LpnStatus.IN_TRANSIT);

        Lpn updated = lpnRepository.save(lpn);

        log.info("LPN {} moved from {} to {}",
                lpnNumber, oldLocation, newLocation);

        return mapToResponse(updated);
    }

    // ================================================
    // UPDATE STATUS
    // ================================================
    @Override
    @Transactional
    public LpnResponse updateStatus(
            String lpnNumber,
            LpnStatus status) {

        Lpn lpn = lpnRepository
                .findByLpnNumber(lpnNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "LPN", lpnNumber));

        lpn.setStatus(status);
        return mapToResponse(lpnRepository.save(lpn));
    }

    // ================================================
    // CANCEL LPN
    // ================================================
    @Override
    @Transactional
    public void cancel(Long id) {

        Lpn lpn = lpnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "LPN", String.valueOf(id)));

        lpn.setStatus(LpnStatus.CANCELLED);
        lpnRepository.save(lpn);

        log.info("LPN cancelled: {}",
                lpn.getLpnNumber());
    }

    // ================================================
    // MAP TO RESPONSE
    // ================================================
    private LpnResponse mapToResponse(Lpn lpn) {
        LpnResponse response =
                modelMapper.map(lpn, LpnResponse.class);
        response.setCreatedAt(lpn.getCreatedAt());
        response.setCreatedBy(lpn.getCreatedBy());
        response.setUpdatedAt(lpn.getUpdatedAt());
        response.setUpdatedBy(lpn.getUpdatedBy());
        return response;
    }
}