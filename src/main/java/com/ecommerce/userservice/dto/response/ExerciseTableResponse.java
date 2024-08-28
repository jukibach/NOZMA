package com.ecommerce.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Builder
public class ExerciseTableResponse {
    private List<ExerciseColumnResponse> columns;
    private int primaryColumnId;
    private List<ExerciseRowResponse> rows;
}
