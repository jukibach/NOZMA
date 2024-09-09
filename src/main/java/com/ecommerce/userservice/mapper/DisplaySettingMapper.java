package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.request.UpdateDisplaySettingPayload;
import com.ecommerce.userservice.dto.response.UpdateDisplaySettingResponse;
import com.ecommerce.userservice.entity.DisplayExerciseSetting;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DisplaySettingMapper {
    UpdateDisplaySettingResponse entityToResponse(DisplayExerciseSetting updateDisplaySetting);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateDisplaySettingPayload updateDisplaySettingPayload,
                      @MappingTarget DisplayExerciseSetting entity);
}
