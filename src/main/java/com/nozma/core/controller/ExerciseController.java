package com.nozma.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nozma.core.constant.ApiURL;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.service.exercises.impl.ExerciseServiceFactory;
import com.nozma.core.util.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiURL.ROOT_PATH)
public class ExerciseController {
    
    private final ExerciseServiceFactory exerciseServiceFactory;
    
    public ExerciseController(
            ExerciseServiceFactory exerciseServiceFactory
    ) {
        this.exerciseServiceFactory = exerciseServiceFactory;
    }
    
    @GetMapping(value = ApiURL.GET_EXERCISE)
    public ResponseEntity<ApiResponse> getExercises(ExercisePagePayload exercisePagePayload)
            throws JsonProcessingException, IllegalAccessException {
        var result = exerciseServiceFactory.getExerciseService(exercisePagePayload.getUserType())
                .getExercises(exercisePagePayload);
        return ResponseEntityUtil.createSuccessfulOkResponse(result);
    }
}
