package com.nozma.core.service.impl;

import com.nozma.core.dto.projections.ExerciseColumnView;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseColumnResponse;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.nozma.core.entity.BaseDomain;
import com.nozma.core.entity.exercises.DisplayExerciseSetting;
import com.nozma.core.enums.ExerciseColumnEnum;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.enums.StatusAndMessage;
import com.nozma.core.exception.BusinessException;
import com.nozma.core.mapper.ExerciseMapper;
import com.nozma.core.mybatis.mapper.MybatisExerciseMapper;
import com.nozma.core.repository.DisplayExerciseSettingRepository;
import com.nozma.core.repository.ExerciseColumnRepository;
import com.nozma.core.service.ExerciseService;
import com.nozma.core.util.CommonUtil;
import com.nozma.core.util.SecurityUtil;
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

@Service
@Slf4j
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    private final ExerciseMapper exerciseMapper;
    
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
        
        
        
        var exerciseRows = mybatisExerciseMapper.selectDynamicFields(visibleColumnCodes, exercisePagePayload,
                List.of("SYSTEM", SecurityUtil.getCurrentAccountName()));
        var totalRowsCount = mybatisExerciseMapper.countExerciseRows(visibleColumnCodes, exercisePagePayload,
                List.of("SYSTEM", SecurityUtil.getCurrentAccountName()));
        
        var exerciseRowResponses = exerciseRows.stream().map(exerciseMapper::exerciseRowToResponse).toList();
        
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
    
    @Override
    public ExerciseTableResponse getExercisesForGuest(ExercisePagePayload exercisePagePayload) {
        var columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        var visibleColumnCodes = columns.stream().map(ExerciseColumnView::getCode).toList();
        
        List<String> orderByClauses = new ArrayList<>();
//        if (CommonUtil.isNonNullOrNonEmpty(sort)) {
//            for (String sortParam : sort) {
//                String[] sortDetails = sortParam.split(",");
//                String sortBy = sortDetails.length > 0
//                        ? ExerciseColumnEnum.valueOf(sortDetails[0]).getField()
//                        : ExerciseColumnEnum.EXERCISE.getField();
//
//                String direction = sortDetails.length > 1 && "desc".equalsIgnoreCase(sortDetails[1])
//                        ? "DESC" : "ASC";
//                orderByClauses.add(sortBy.concat(direction));
//            }
//        }
//
//        // Join the clauses to create the final orderBy string
//        String orderBy = CommonUtil.isNonNullOrNonEmpty(orderByClauses)
//                ? String.join(", ", orderByClauses)
//                : null;
        
        var exerciseRows = mybatisExerciseMapper.selectDynamicFields(visibleColumnCodes, exercisePagePayload,
                Collections.singletonList("SYSTEM"));
        var totalRowsCount = mybatisExerciseMapper.countExerciseRows(visibleColumnCodes, exercisePagePayload,
                Collections.singletonList("SYSTEM"));
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponsesForGuest(columns);
        var exerciseRowResponses = exerciseRows.stream().map(exerciseMapper::exerciseRowToResponse).toList();
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponsesForGuest(List<ExerciseColumnView> columns) {
        return columns.stream().map(columnView -> new ExerciseColumnResponse(columnView.getCode(), columnView.getName(),
                columnView.getType())).toList();
    }
}
