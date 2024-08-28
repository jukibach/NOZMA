package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.dto.response.ExerciseTableResponse;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.entity.ExerciseEquipment;
import com.ecommerce.userservice.entity.ExerciseMovementPattern;
import com.ecommerce.userservice.entity.ExerciseMuscleGroup;
import com.ecommerce.userservice.enums.ExerciseColumnEnum;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.CustomExerciseMapper;
import com.ecommerce.userservice.mapper.ExerciseMapper;
import com.ecommerce.userservice.mybatis.mapper.MybatisExerciseMapper;
import com.ecommerce.userservice.repository.ExerciseColumnRepository;
import com.ecommerce.userservice.repository.ExerciseEquipmentRepository;
import com.ecommerce.userservice.repository.ExerciseMovementPatternRepository;
import com.ecommerce.userservice.repository.ExerciseMuscleGroupRepository;
import com.ecommerce.userservice.repository.ExerciseRepository;
import com.ecommerce.userservice.repository.UserExerciseSettingRepository;
import com.ecommerce.userservice.service.ExerciseService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final UserExerciseSettingRepository userExerciseSettingRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    
    @Override
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload) throws JsonProcessingException {
        // TODO:
        //  paging
        //  store setting (user, pageId)
        //
        
        // If user is logged in
        if (CommonUtil.isNullOrEmpty(SecurityUtil.getCurrentAccountId())) {
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        }
        var userExerciseSettings = userExerciseSettingRepository.findByStatusAndAccountIdAndCode(RecordStatus.ACTIVE,
                SecurityUtil.getCurrentAccountId(), "exercises");
        var objectMapper = new ObjectMapper();
        var columnSettings = objectMapper.readValue(userExerciseSettings.getSettings(),
                new TypeReference<Map<String, Boolean>>() {
                });
        
        List<String> columnCodes = new ArrayList<>();
        columnSettings.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                columnCodes.add(key);
            }
        });
        
        List<String> nonListExerciseColumns = getNonListExerciseColumns(columnCodes);
        var exercises = mybatisExerciseMapper.selectFields(nonListExerciseColumns, exercisePagePayload);
        var exerciseIds = exercises.stream().map(ExerciseRowResponse::getId).toList();
        var columns = exerciseColumnRepository.findAllByStatusAndCodeIn(RecordStatus.ACTIVE, columnCodes);
        
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfEquipmentName = null;
        if (columnCodes.contains("equipment")) {
            var exerciseEquipments = exerciseEquipmentRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                    exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
            );
            groupByExerciseIdAndReturnListOfEquipmentName =
                    exerciseEquipments.stream().collect(Collectors.groupingBy(ExerciseEquipment::getExerciseId,
                            Collectors.mapping(ExerciseEquipment::getEquipmentName, Collectors.toList())));
        }
        
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfMovementPatternName = null;
        if (columnCodes.contains("movementPatterns")) {
            var exerciseMovementPatterns = exerciseMovementPatternRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                    exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
            );
            groupByExerciseIdAndReturnListOfMovementPatternName = exerciseMovementPatterns.stream()
                    .collect(Collectors.groupingBy(ExerciseMovementPattern::getExerciseId,
                            Collectors.mapping(ExerciseMovementPattern::getPatternName,
                                    Collectors.toList())));
        }
        
        Map<Long, List<String>> groupByExerciseIdAndReturnListOfMuscleGroupName = null;
        if (columnCodes.contains("muscleGroup")) {
            var exerciseMuscleGroups = exerciseMuscleGroupRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                    exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
            );
            groupByExerciseIdAndReturnListOfMuscleGroupName = exerciseMuscleGroups.stream()
                    .collect(Collectors.groupingBy(ExerciseMuscleGroup::getExerciseId,
                            Collectors.mapping(ExerciseMuscleGroup::getMuscleGroupName,
                                    Collectors.toList())));
        }
        
        var exerciseColumns = exerciseMapper.viewToResponse(columns);
        
        var exerciseConverted = customExerciseMapper.convertToExerciseRowResponse(exercises,
                groupByExerciseIdAndReturnListOfEquipmentName,
                groupByExerciseIdAndReturnListOfMovementPatternName,
                groupByExerciseIdAndReturnListOfMuscleGroupName);
        
        
        // TODO: Exercises created by users
        return ExerciseTableResponse.builder()
                .columns(exerciseColumns)
                .primaryColumnId(ExerciseColumnEnum.EXERCISE.ordinal() + 1)
                .rows(exerciseConverted)
                .build();
    }
    
    private static List<String> getNonListExerciseColumns(List<String> columnCodes) {
        List<String> nonListExerciseColumns = new ArrayList<>();
        if (columnCodes.contains(Exercise.Fields.name)) {
            nonListExerciseColumns.add(Exercise.Fields.name);
        }
        if (columnCodes.contains(Exercise.Fields.description)) {
            nonListExerciseColumns.add(Exercise.Fields.description);
        }
        if (columnCodes.contains(Exercise.Fields.majorMuscle)) {
            nonListExerciseColumns.add(Exercise.Fields.majorMuscle);
        }
        if (columnCodes.contains(Exercise.Fields.mechanics)) {
            nonListExerciseColumns.add(Exercise.Fields.mechanics);
        }
        if (columnCodes.contains(Exercise.Fields.bodyRegion)) {
            nonListExerciseColumns.add(Exercise.Fields.bodyRegion);
        }
        if (columnCodes.contains(Exercise.Fields.laterality)) {
            nonListExerciseColumns.add(Exercise.Fields.laterality);
        }
        if (columnCodes.contains("exerciseType")) {
            nonListExerciseColumns.add(Exercise.Fields.isCardio);
            nonListExerciseColumns.add(Exercise.Fields.isPlyo);
            nonListExerciseColumns.add(Exercise.Fields.isWeight);
        }
        return nonListExerciseColumns;
    }
}
