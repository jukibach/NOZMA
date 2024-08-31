package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.ExerciseRow;
import com.ecommerce.userservice.dto.response.ExerciseColumnResponse;
import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.projection.ExerciseColumnView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {
    List<ExerciseColumnResponse> viewToResponse(List<ExerciseColumnView> column);
    
    ExerciseRowResponse exerciseRowToResponse(ExerciseRow exerciseRow);
    
}
