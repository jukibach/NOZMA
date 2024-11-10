package com.nozma.core.service.exercises.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.nozma.core.service.exercises.AdminExerciseService;
import com.nozma.core.service.exercises.NormalUserExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminExerciseServiceImpl implements AdminExerciseService {
    
    private final NormalUserExerciseService normalUserExerciseService;
    
    @Autowired
    public AdminExerciseServiceImpl(
            NormalUserExerciseService normalUserExerciseService) {
        this.normalUserExerciseService = normalUserExerciseService;
    }
    
    @Override
    @Transactional(
            rollbackFor = Exception.class
            , propagation = Propagation.REQUIRED
            , isolation = Isolation.READ_COMMITTED
    )
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload)
            throws IllegalAccessException, JsonProcessingException
    {
        return normalUserExerciseService.getExercises(exercisePagePayload);
    }
    
    @Override
    public void addExercise() {
        // TODO document why this method is empty
    }
}
