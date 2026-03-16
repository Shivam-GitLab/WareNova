package com.warenova.wms.modules.master.lpn.dto;

import com.warenova.wms.common.enums.LpnStatus;
import com.warenova.wms.common.enums.LpnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LpnResponse {

    private Long id;
    private String lpnNumber;
    private LpnType lpnType;
    private String warehouseCode;
    private String currentLocation;
    private String parentLpnNumber;
    private String asnNumber;
    private String poNumber;
    private Double grossWeightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private LpnStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}