package com.warenova.wms.modules.master.lpn.dto;

import com.warenova.wms.common.enums.LpnType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LpnRequest {

    @NotBlank(message = "LPN number is required")
    private String lpnNumber;

    @NotNull(message = "LPN type is required")
    private LpnType lpnType;

    @NotBlank(message = "Warehouse code is required")
    private String warehouseCode;

    private String currentLocation;
    private String parentLpnNumber;
    private String asnNumber;
    private String poNumber;
    private Double grossWeightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
}