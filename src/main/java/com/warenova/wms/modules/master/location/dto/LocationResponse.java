package com.warenova.wms.modules.master.location.dto;

import com.warenova.wms.common.enums.LocationStatus;
import com.warenova.wms.common.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ================================================
// LOCATION RESPONSE DTO
// ================================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {

    private Long id;
    private String warehouseCode;
    private String locationCode;
    private String zone;
    private String aisle;
    private String bay;
    private String level;
    private LocationType locationType;
    private LocationStatus status;
    private Double maxWeightKg;
    private Double maxVolumeCbm;
    private Boolean allowMixedSku;
    private Boolean active;

    // ── Audit fields ──────────────────────────────
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}