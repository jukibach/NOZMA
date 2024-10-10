package com.nozma.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nozma.core.constant.ApiURL;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.service.ExerciseService;
import com.nozma.core.util.ResponseEntityUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(ApiURL.ROOT_PATH)
public class ExerciseController {
    private final ExerciseService exerciseService;
    
    @GetMapping(value = ApiURL.GET_EXERCISE)
    public ResponseEntity<ApiResponse> getExercises(
            ExercisePagePayload exercisePagePayload
    ) throws JsonProcessingException, IllegalAccessException {
        return ResponseEntityUtil.createSuccessfulOkResponse(exerciseService.getExercises(exercisePagePayload));
    }
    
    @GetMapping(value = ApiURL.GET_EXERCISE_GUEST)
    public ResponseEntity<ApiResponse> getExercisesForGuest(ExercisePagePayload exercisePagePayload) {
        return ResponseEntityUtil.createSuccessfulOkResponse(exerciseService.getExercisesForGuest(exercisePagePayload));
    }
}
