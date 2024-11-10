package com.nozma.core.service.exercises;

import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public sealed interface ExerciseService permits GuestExerciseService, AdminExerciseService, NormalUserExerciseService {
    ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload)
            throws JsonProcessingException, IllegalAccessException;
}
