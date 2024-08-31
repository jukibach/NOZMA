package com.ecommerce.userservice.dto.response;

import java.util.List;

public record ExerciseTableResponse(List<ExerciseColumnResponse> columns, int primaryColumnId,
                                    List<ExerciseRowResponse> rows, Integer totalRowCount) {
    
}
