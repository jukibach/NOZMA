package com.ecommerce.userservice.dto.request;

import com.ecommerce.userservice.annotations.CustomNotNull;
import com.ecommerce.userservice.constant.FieldNamesConstant;
import com.ecommerce.userservice.constant.MessageConstant;
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
