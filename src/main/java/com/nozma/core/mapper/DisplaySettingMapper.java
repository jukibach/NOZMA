package com.nozma.core.mapper;

import com.nozma.core.dto.request.UpdateDisplaySettingPayload;
import com.nozma.core.dto.response.UpdateDisplaySettingResponse;
import com.nozma.core.entity.exercises.DisplayExerciseSetting;
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
