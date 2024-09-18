package com.nozma.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDisplaySettingPayload {
    private Boolean bodyRegion;
    private Boolean laterality;
    private Boolean majorMuscle;
    private Boolean mechanics;
    private Boolean equipments;
    private Boolean exerciseTypes;
    private Boolean muscleGroup;
    private Boolean movementPatterns;
    private Boolean description;
}
