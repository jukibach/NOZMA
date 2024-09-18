package com.nozma.core.service;

import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ExerciseService {
    ExerciseTableResponse getExercises(
            ExercisePagePayload exercisePagePayload) throws JsonProcessingException, IllegalAccessException;
    
    ExerciseTableResponse getExercisesForGuest(ExercisePagePayload exercisePagePayload);
}
