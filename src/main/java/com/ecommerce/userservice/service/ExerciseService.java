package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.dto.response.ExerciseTableResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ExerciseService {
    ExerciseTableResponse getExercises(
            ExercisePagePayload exercisePagePayload) throws JsonProcessingException, IllegalAccessException;
    
    ExerciseTableResponse getExercisesForGuest(ExercisePagePayload exercisePagePayload);
}
