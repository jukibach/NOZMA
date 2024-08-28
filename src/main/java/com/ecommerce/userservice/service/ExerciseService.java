package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.response.ExerciseTableResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ExerciseService {
    ExerciseTableResponse getExercises() throws JsonProcessingException;
}
