package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.dto.response.ExerciseTableResponse;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.enums.ExerciseColumnEnum;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.mapper.CustomExerciseMapper;
import com.ecommerce.userservice.mapper.ExerciseMapper;
import com.ecommerce.userservice.repository.ExerciseColumnRepository;
import com.ecommerce.userservice.repository.ExerciseEquipmentRepository;
import com.ecommerce.userservice.repository.ExerciseMovementPatternRepository;
import com.ecommerce.userservice.repository.ExerciseMuscleGroupRepository;
import com.ecommerce.userservice.repository.ExerciseRepository;
import com.ecommerce.userservice.service.ExerciseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final CustomExerciseMapper customExerciseMapper;
    private final ExerciseMovementPatternRepository exerciseMovementPatternRepository;
    private final ExerciseEquipmentRepository exerciseEquipmentRepository;
    private final ExerciseMuscleGroupRepository exerciseMuscleGroupRepository;
    
    @Override
    public ExerciseTableResponse getExercises() {
        var columns = exerciseColumnRepository.findAll();
        var exercises = exerciseRepository.findAllByStatusAndCreatedByIn(RecordStatus.ACTIVE,
                Collections.singletonList("SYSTEM"));
        var exerciseIds = exercises.stream().map(Exercise::getId).toList();
        
        var exerciseEquipments = exerciseEquipmentRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
        );
        var exerciseMovementPatterns = exerciseMovementPatternRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
        );
        var exerciseMuscleGroups = exerciseMuscleGroupRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
        );
        
        var exerciseConverted = customExerciseMapper.convertToExerciseRowResponse(exercises,
                exerciseEquipments, exerciseMovementPatterns, exerciseMuscleGroups, columns);
        
        var exerciseColumns = exerciseMapper.entityToResponse(columns);
        
        // TODO: Exercises created by users
        return ExerciseTableResponse.builder()
                .columns(exerciseColumns)
                .primaryColumnId(ExerciseColumnEnum.EXERCISE.getColumnId())
                .rows(exerciseConverted)
                .build();
    }
}
