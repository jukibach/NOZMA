package com.ecommerce.userservice.mapper;


import com.ecommerce.userservice.dto.response.ExerciseColumnResponse;
import com.ecommerce.userservice.entity.ExerciseColumn;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {
    List<ExerciseColumnResponse> entityToResponse(List<ExerciseColumn> column);
    
    
}
