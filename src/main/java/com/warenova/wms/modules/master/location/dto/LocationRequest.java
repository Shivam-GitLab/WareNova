package com.warenova.wms.modules.master.location.dto;

import com.warenova.wms.common.enums.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ================================================
// LOCATION REQUEST DTO
// ================================================

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    @NotBlank(message = "Location code is required")
    private String locationCode;

    private String zone;
    private String aisle;
    private String bay;
    private String level;

    @NotNull(message = "Location type is required")
    private LocationType locationType;

    private Double maxWeightKg;
    private Double maxVolumeCbm;

    @NotNull(message = "Mixed SKU flag is required")
    private Boolean allowMixedSku;
}