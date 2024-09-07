package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.dto.ExerciseRow;
import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.dto.response.ExerciseColumnResponse;
import com.ecommerce.userservice.dto.response.ExerciseTableResponse;
import com.ecommerce.userservice.entity.BaseDomain;
import com.ecommerce.userservice.entity.DisplayExerciseSetting;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.entity.ExerciseEquipment;
import com.ecommerce.userservice.entity.ExerciseMovementPattern;
import com.ecommerce.userservice.entity.ExerciseMuscleGroup;
import com.ecommerce.userservice.enums.ExerciseColumnEnum;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.mapper.CustomExerciseMapper;
import com.ecommerce.userservice.mybatis.mapper.MybatisExerciseMapper;
import com.ecommerce.userservice.projection.ExerciseColumnView;
import com.ecommerce.userservice.repository.DisplayExerciseSettingRepository;
import com.ecommerce.userservice.repository.ExerciseColumnRepository;
import com.ecommerce.userservice.repository.ExerciseEquipmentRepository;
import com.ecommerce.userservice.repository.ExerciseMovementPatternRepository;
import com.ecommerce.userservice.repository.ExerciseMuscleGroupRepository;
import com.ecommerce.userservice.service.ExerciseService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final CustomExerciseMapper customExerciseMapper;
    private final ExerciseMovementPatternRepository exerciseMovementPatternRepository;
    private final ExerciseEquipmentRepository exerciseEquipmentRepository;
    private final ExerciseMuscleGroupRepository exerciseMuscleGroupRepository;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    
    @Override
    @Transactional
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload) throws IllegalAccessException {
        // TODO:
        //  paging
        //  store setting (user, pageId)
        //  Exercises created by users
        
        // If user is logged in
        if (CommonUtil.isNullOrEmpty(SecurityUtil.getCurrentAccountId()))
            throw new BusinessException(StatusAndMessage.ACCOUNT_DOES_NOT_EXIST);
        
        var displayExerciseSettings = displayExerciseSettingRepository.findByAccountIdAndCode(
                SecurityUtil.getCurrentAccountId(), "exercises");
        List<ExerciseColumnView> columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        if (CommonUtil.isNullOrEmpty(displayExerciseSettings))
            displayExerciseSettings = DisplayExerciseSetting.builder()
                    .accountId(SecurityUtil.getCurrentAccountId())
                    .code("exercises")
                    .name(true)
                    .bodyRegion(true)
                    .laterality(true)
                    .majorMuscle(true)
                    .mechanics(true)
                    .equipments(true)
                    .exerciseTypes(true)
                    .muscleGroup(true)
                    .movementPatterns(true)
                    .description(true)
                    .build();
        
        if (RecordStatus.INACTIVE.equals(displayExerciseSettings.getStatus()))
            displayExerciseSettings.setStatus(RecordStatus.ACTIVE);
        
        // IF there are no changes, does it execute this code ??
        displayExerciseSettings = displayExerciseSettingRepository.save(displayExerciseSettings);
        
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponses(displayExerciseSettings, columns);
        var visibleColumnCodes = exerciseColumnResponses.stream().filter(ExerciseColumnResponse::visible)
                .map(ExerciseColumnResponse::code).toList();
        var nonListColumns = getNonListExerciseVisibleColumns(visibleColumnCodes);
        var exerciseRows = mybatisExerciseMapper.selectFields(nonListColumns, exercisePagePayload);
        var totalRowsCount = mybatisExerciseMapper.countExerciseRows(nonListColumns, exercisePagePayload);
        var exerciseIds = exerciseRows.stream().map(ExerciseRow::getId).toList();
        
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
        
        var exerciseRowResponses = customExerciseMapper.convertToExerciseRowResponse(exerciseRows, exerciseEquipments,
                exerciseMovementPatterns, exerciseMuscleGroups);
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponses(DisplayExerciseSetting displayExerciseSetting,
                                                            List<ExerciseColumnView> columns)
            throws IllegalAccessException {
        List<ExerciseColumnResponse> exerciseColumnResponseList = new ArrayList<>();
        List<String> exclusiveFields = List.of(
                DisplayExerciseSetting.Fields.id,
                DisplayExerciseSetting.Fields.accountId,
                DisplayExerciseSetting.Fields.code,
                BaseDomain.Fields.status,
                BaseDomain.Fields.createdDate,
                BaseDomain.Fields.createdBy,
                BaseDomain.Fields.updatedDate,
                BaseDomain.Fields.updatedBy
        );
        List<Field> fields = Arrays.stream(displayExerciseSetting.getClass().getDeclaredFields())
                .filter(field -> !exclusiveFields.contains(field.getName()))
                .toList();
        
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            // Get the field's name and value
            boolean value = (Boolean) field.get(displayExerciseSetting);
            ExerciseColumnView exerciseColumnView = columns.stream()
                    .filter(column -> column.getCode().equals(field.getName()))
                    .findFirst()
                    .orElseThrow();
            exerciseColumnResponseList.add(new ExerciseColumnResponse(field.getName(), exerciseColumnView.getName()
                    , exerciseColumnView.getType(), value));
        }
        return exerciseColumnResponseList;
    }
    
    private List<String> getNonListExerciseVisibleColumns(List<String> columnCodes) {
        List<String> exclusiveCodes = Stream.of(ExerciseColumnEnum.EXERCISE_TYPE,
                        ExerciseColumnEnum.EQUIPMENT, ExerciseColumnEnum.MOVEMENT_PATTERNS)
                .map(ExerciseColumnEnum::getCode).toList();
        
        List<String> nonListExerciseColumns = new ArrayList<>(columnCodes.stream()
                .filter(columnCode -> !exclusiveCodes.contains(columnCode)).toList());
        
        if (columnCodes.contains(ExerciseColumnEnum.EXERCISE_TYPE.getCode())) {
            nonListExerciseColumns.add(Exercise.Fields.isCardio);
            nonListExerciseColumns.add(Exercise.Fields.isPlyo);
            nonListExerciseColumns.add(Exercise.Fields.isWeight);
        }
        
        return nonListExerciseColumns;
    }
    
    @Override
    public ExerciseTableResponse getExercisesForGuest(ExercisePagePayload exercisePagePayload) {
        var columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        var visibleColumnCodes = columns.stream().map(ExerciseColumnView::getCode).toList();
        var nonListColumns = getNonListExerciseVisibleColumns(visibleColumnCodes);
        var exerciseRows = mybatisExerciseMapper.selectFields(nonListColumns, exercisePagePayload);
        var totalRowsCount = mybatisExerciseMapper.countExerciseRows(nonListColumns, exercisePagePayload);
        var exerciseIds = exerciseRows.stream().map(ExerciseRow::getId).toList();
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponsesForGuest(columns);
        
        List<ExerciseEquipment> exerciseEquipments =
                exerciseEquipmentRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                );
        
        List<ExerciseMovementPattern> exerciseMovementPatterns =
                exerciseMovementPatternRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                );
        
        List<ExerciseMuscleGroup> exerciseMuscleGroups =
                exerciseMuscleGroupRepository.findAllByExerciseIdInAndCreatedByInAndStatus(
                        exerciseIds, Collections.singletonList("SYSTEM"), RecordStatus.ACTIVE
                );
        
        var exerciseRowResponses = customExerciseMapper.convertToExerciseRowResponse(exerciseRows, exerciseEquipments,
                exerciseMovementPatterns, exerciseMuscleGroups);
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponsesForGuest(List<ExerciseColumnView> columns) {
        return columns.stream().map(columnView -> new ExerciseColumnResponse(columnView.getCode(), columnView.getName(),
                columnView.getType())).toList();
    }
}
