package com.warenova.wms.modules.inbound.purchaseorder.service.impl;

import com.warenova.wms.common.enums.POStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.common.exception.WMSException;
import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderRequest;
import com.warenova.wms.modules.inbound.purchaseorder.dto.PurchaseOrderResponse;
import com.warenova.wms.modules.inbound.purchaseorder.entity.PurchaseOrder;
import com.warenova.wms.modules.inbound.purchaseorder.repository.PurchaseOrderRepository;
import com.warenova.wms.modules.inbound.purchaseorder.service.PurchaseOrderService;
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
public class PurchaseOrderServiceImpl
        implements PurchaseOrderService {

    private final PurchaseOrderRepository poRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE PO
    // ================================================
    @Override
    @Transactional
    public PurchaseOrderResponse create(
            PurchaseOrderRequest request) {

        log.info("Creating PO for SKU: {} " +
                        "Supplier: {}",
                request.getSku(),
                request.getSupplierCode());

        String poNumber = generatePoNumber();

        // ── Calculate total amount ────────────────
        double totalAmount = 0.0;
        if (request.getUnitPrice() != null) {
            totalAmount = request.getOrderedQuantity()
                    * request.getUnitPrice();
        }

        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNumber)
                .warehouseCode(
                        request.getWarehouseCode().toUpperCase())
                .supplierCode(
                        request.getSupplierCode().toUpperCase())
                .sku(request.getSku().toUpperCase())
                .orderedQuantity(request.getOrderedQuantity())
                .receivedQuantity(0.0)
                .pendingQuantity(request.getOrderedQuantity())
                .unitPrice(request.getUnitPrice())
                .totalAmount(totalAmount)
                .orderDate(request.getOrderDate())
                .expectedDeliveryDate(
                        request.getExpectedDeliveryDate())
                .notes(request.getNotes())
                .status(POStatus.DRAFT)
                .build();

        PurchaseOrder saved = poRepository.save(po);

        log.info("PO created: {}", saved.getPoNumber());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getById(Long id) {
        return mapToResponse(
                poRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "PurchaseOrder",
                                        String.valueOf(id)))
        );
    }

    // ================================================
    // GET BY PO NUMBER
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getByPoNumber(
            String poNumber) {
        return mapToResponse(
                poRepository.findByPoNumber(poNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "PurchaseOrder", poNumber))
        );
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getAllByWarehouse(
            String warehouseCode) {
        return poRepository
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
    public List<PurchaseOrderResponse> getByStatus(
            String warehouseCode,
            String status) {
        return poRepository
                .findByWarehouseCodeAndStatus(
                        warehouseCode,
                        POStatus.valueOf(status))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // CONFIRM PO
    // ================================================
    @Override
    @Transactional
    public PurchaseOrderResponse confirm(Long id) {

        PurchaseOrder po = getPoOrThrow(id);

        if (po.getStatus() != POStatus.DRAFT) {
            throw new WMSException(
                    "Only DRAFT PO can be confirmed"
            );
        }

        po.setStatus(POStatus.CONFIRMED);
        log.info("PO confirmed: {}", po.getPoNumber());
        return mapToResponse(poRepository.save(po));
    }

    // ================================================
    // CLOSE PO
    // ================================================
    @Override
    @Transactional
    public PurchaseOrderResponse close(Long id) {

        PurchaseOrder po = getPoOrThrow(id);
        po.setStatus(POStatus.CLOSED);
        po.setActualDeliveryDate(LocalDate.now());

        log.info("PO closed: {}", po.getPoNumber());
        return mapToResponse(poRepository.save(po));
    }

    // ================================================
    // CANCEL PO
    // ================================================
    @Override
    @Transactional
    public PurchaseOrderResponse cancel(Long id) {

        PurchaseOrder po = getPoOrThrow(id);

        if (po.getStatus() == POStatus.CLOSED ||
                po.getStatus() == POStatus.FULLY_RECEIVED) {
            throw new WMSException(
                    "Closed/Received PO cannot be cancelled"
            );
        }

        po.setStatus(POStatus.CANCELLED);
        log.info("PO cancelled: {}", po.getPoNumber());
        return mapToResponse(poRepository.save(po));
    }

    // ================================================
    // UPDATE PO
    // ================================================
    @Override
    @Transactional
    public PurchaseOrderResponse update(
            Long id,
            PurchaseOrderRequest request) {

        PurchaseOrder po = getPoOrThrow(id);

        if (po.getStatus() != POStatus.DRAFT) {
            throw new WMSException(
                    "Only DRAFT PO can be updated"
            );
        }

        po.setOrderedQuantity(
                request.getOrderedQuantity());
        po.setPendingQuantity(
                request.getOrderedQuantity()
                        - po.getReceivedQuantity());
        po.setUnitPrice(request.getUnitPrice());
        po.setExpectedDeliveryDate(
                request.getExpectedDeliveryDate());
        po.setNotes(request.getNotes());

        if (request.getUnitPrice() != null) {
            po.setTotalAmount(
                    request.getOrderedQuantity()
                            * request.getUnitPrice());
        }

        return mapToResponse(poRepository.save(po));
    }

    // ================================================
    // PRIVATE HELPERS
    // ================================================
    private PurchaseOrder getPoOrThrow(Long id) {
        return poRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "PurchaseOrder",
                                String.valueOf(id)));
    }

    private String generatePoNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("yyyyMMddHHmmss"));
        return "PO-" + timestamp;
    }

    private PurchaseOrderResponse mapToResponse(
            PurchaseOrder po) {
        PurchaseOrderResponse response =
                modelMapper.map(po,
                        PurchaseOrderResponse.class);
        response.setCreatedAt(po.getCreatedAt());
        response.setCreatedBy(po.getCreatedBy());
        response.setUpdatedAt(po.getUpdatedAt());
        response.setUpdatedBy(po.getUpdatedBy());
        return response;
    }
}