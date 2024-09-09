package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.constant.ApiURL;
import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.service.ExerciseService;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            ExercisePagePayload exercisePagePayload) throws JsonProcessingException,
            IllegalAccessException {
        return ResponseEntityUtil.createSuccessfulOkResponse(exerciseService.getExercises(exercisePagePayload));
    }
    
    @GetMapping(value = ApiURL.GET_EXERCISE_GUEST)
    public ResponseEntity<ApiResponse> getExercisesForGuest(ExercisePagePayload exercisePagePayload) {
        return ResponseEntityUtil.createSuccessfulOkResponse(exerciseService.getExercisesForGuest(exercisePagePayload));
    }
}
