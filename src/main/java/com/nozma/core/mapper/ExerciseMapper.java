package com.nozma.core.mapper;

import com.nozma.core.dto.ExerciseRow;
import com.nozma.core.dto.response.ExerciseColumnResponse;
import com.nozma.core.dto.response.ExerciseRowResponse;
import com.nozma.core.dto.projections.ExerciseColumnView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExerciseMapper {
    
    ExerciseRowResponse exerciseRowToResponse(ExerciseRow exerciseRow);
    
}
