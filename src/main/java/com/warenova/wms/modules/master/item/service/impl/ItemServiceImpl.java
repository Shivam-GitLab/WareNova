package com.warenova.wms.modules.master.item.service.impl;

import com.warenova.wms.common.enums.ItemStatus;
import com.warenova.wms.common.exception.DuplicateResourceException;
import com.warenova.wms.common.exception.ResourceNotFoundException;
import com.warenova.wms.modules.master.item.dto.ItemRequest;
import com.warenova.wms.modules.master.item.dto.ItemResponse;
import com.warenova.wms.modules.master.item.entity.Item;
import com.warenova.wms.modules.master.item.repository.ItemRepository;
import com.warenova.wms.modules.master.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// ================================================
// ITEM SERVICE IMPLEMENTATION
// ================================================

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    // ================================================
    // CREATE ITEM
    // ================================================
    @Override
    @Transactional
    public ItemResponse create(ItemRequest request) {

        log.info("Creating item with SKU: {}",
                request.getSku());

        // ── Check duplicate SKU ───────────────────
        if (itemRepository.existsBySku(
                request.getSku())) {
            throw new DuplicateResourceException(
                    "Item", "SKU", request.getSku()
            );
        }

        // ── Build entity ──────────────────────────
        Item item = Item.builder()
                .sku(request.getSku().toUpperCase())
                .itemName(request.getItemName())
                .description(request.getDescription())
                .category(request.getCategory())
                .subCategory(request.getSubCategory())
                .brand(request.getBrand())
                .uom(request.getUom().toUpperCase())
                .warehouseCode(
                        request.getWarehouseCode()
                                .toUpperCase())
                .weightKg(request.getWeightKg())
                .lengthCm(request.getLengthCm())
                .widthCm(request.getWidthCm())
                .heightCm(request.getHeightCm())
                .lotTracking(request.getLotTracking())
                .serialTracking(request.getSerialTracking())
                .expiryTracking(request.getExpiryTracking())
                .reorderLevel(request.getReorderLevel())
                .minStockLevel(request.getMinStockLevel())
                .maxStockLevel(request.getMaxStockLevel())
                .unitPrice(request.getUnitPrice())
                .status(ItemStatus.ACTIVE)
                .build();

        Item saved = itemRepository.save(item);

        log.info("Item created with SKU: {}",
                saved.getSku());

        return mapToResponse(saved);
    }

    // ================================================
    // GET BY ID
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public ItemResponse getById(Long id) {

        Item item = itemRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item", String.valueOf(id)
                        )
                );
        return mapToResponse(item);
    }

    // ================================================
    // GET BY SKU
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public ItemResponse getBySku(String sku) {

        Item item = itemRepository
                .findBySku(sku)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item", sku
                        )
                );
        return mapToResponse(item);
    }

    // ================================================
    // GET ALL BY WAREHOUSE
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getAllByWarehouse(
            String warehouseCode) {

        return itemRepository
                .findByWarehouseCode(warehouseCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // SEARCH ITEMS
    // ================================================
    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> searchItems(
            String warehouseCode,
            String search) {

        return itemRepository
                .searchItems(warehouseCode, search)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================================
    // UPDATE ITEM
    // ================================================
    @Override
    @Transactional
    public ItemResponse update(
            Long id,
            ItemRequest request) {

        log.info("Updating item id: {}", id);

        Item item = itemRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item", String.valueOf(id)
                        )
                );

        // ── SKU cannot be changed ─────────────────
        item.setItemName(request.getItemName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setSubCategory(request.getSubCategory());
        item.setBrand(request.getBrand());
        item.setUom(request.getUom());
        item.setWeightKg(request.getWeightKg());
        item.setLengthCm(request.getLengthCm());
        item.setWidthCm(request.getWidthCm());
        item.setHeightCm(request.getHeightCm());
        item.setLotTracking(request.getLotTracking());
        item.setSerialTracking(
                request.getSerialTracking());
        item.setExpiryTracking(
                request.getExpiryTracking());
        item.setReorderLevel(request.getReorderLevel());
        item.setMinStockLevel(request.getMinStockLevel());
        item.setMaxStockLevel(request.getMaxStockLevel());
        item.setUnitPrice(request.getUnitPrice());

        Item updated = itemRepository.save(item);

        return mapToResponse(updated);
    }

    // ================================================
    // DEACTIVATE ITEM
    // ================================================
    @Override
    @Transactional
    public void deactivate(Long id) {

        Item item = itemRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item", String.valueOf(id)
                        )
                );

        item.setStatus(ItemStatus.INACTIVE);
        itemRepository.save(item);

        log.info("Item deactivated: {}", item.getSku());
    }

    // ================================================
    // MAP TO RESPONSE (PRIVATE HELPER)
    // ================================================
    private ItemResponse mapToResponse(Item item) {

        ItemResponse response =
                modelMapper.map(item, ItemResponse.class);

        response.setCreatedAt(item.getCreatedAt());
        response.setCreatedBy(item.getCreatedBy());
        response.setUpdatedAt(item.getUpdatedAt());
        response.setUpdatedBy(item.getUpdatedBy());

        return response;
    }
}