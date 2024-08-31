package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.ExerciseRow;
import com.ecommerce.userservice.dto.request.ExercisePagePayload;
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

@Service
@Slf4j
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
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
        //  Exercises created by users
        
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
        
        var visibleColumnCodes = new ArrayList<String>();
        columnSettings.forEach((key, value) -> {
            if (Boolean.TRUE.equals(value)) {
                visibleColumnCodes.add(key);
            }
        });
        
        var nonListExerciseColumns = getNonListExerciseVisibleColumns(visibleColumnCodes);
        var exerciseRows = mybatisExerciseMapper.selectFields(nonListExerciseColumns, exercisePagePayload);
        var exerciseIds = exerciseRows.stream().map(ExerciseRow::getId).toList();
        var columns = exerciseColumnRepository.findAllByStatusAndCodeIn(RecordStatus.ACTIVE, visibleColumnCodes);
        
        List<ExerciseEquipment> exerciseEquipments = visibleColumnCodes.contains(
                ExerciseColumnEnum.EQUIPMENT.getCode()) ?
                exerciseEquipmentRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                ) : null;
        
        List<ExerciseMovementPattern> exerciseMovementPatterns = visibleColumnCodes.contains(
                ExerciseColumnEnum.MOVEMENT_PATTERNS.getCode()) ?
                exerciseMovementPatternRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                ) : null;
        
        List<ExerciseMuscleGroup> exerciseMuscleGroups = visibleColumnCodes.contains(
                ExerciseColumnEnum.MUSCLE_GROUP.getCode()) ?
                exerciseMuscleGroupRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                ) : null;
        
        var exerciseColumns = exerciseMapper.viewToResponse(columns);
        
        var exerciseRowResponses = customExerciseMapper.convertToExerciseRowResponse(exerciseRows,
                exerciseEquipments,
                exerciseMovementPatterns,
                exerciseMuscleGroups);
        
        return new ExerciseTableResponse(exerciseColumns, ExerciseColumnEnum.EXERCISE.ordinal() + 1,
                exerciseRowResponses, exerciseRowResponses.size());
    }
    
    private static List<String> getNonListExerciseVisibleColumns(List<String> columnCodes) {
        List<String> nonListExerciseColumns = new ArrayList<>();
        if (columnCodes.contains(ExerciseColumnEnum.EXERCISE.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.name);
        }
        if (columnCodes.contains(ExerciseColumnEnum.DESCRIPTION.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.description);
        }
        if (columnCodes.contains(ExerciseColumnEnum.MAJOR_MUSCLE.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.majorMuscle);
        }
        if (columnCodes.contains(ExerciseColumnEnum.MECHANIC.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.mechanics);
        }
        if (columnCodes.contains(ExerciseColumnEnum.BODY_REGION.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.bodyRegion);
        }
        if (columnCodes.contains(ExerciseColumnEnum.LATERALITY.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.laterality);
        }
        if (columnCodes.contains(ExerciseColumnEnum.EXERCISE_TYPE.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.isCardio);
            nonListExerciseColumns.add(Exercise.Fields.isPlyo);
            nonListExerciseColumns.add(Exercise.Fields.isWeight);
        }
        return nonListExerciseColumns;
    }
}
