package com.nozma.core.dto.response;

import java.util.List;

public record ExerciseTableResponse(List<ExerciseColumnResponse> columns,
                                    List<ExerciseRowResponse> rows, Integer totalRowCount) {
    
}
