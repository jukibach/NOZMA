package com.nozma.core.service.impl;

import com.nozma.core.constant.Constant;
import com.nozma.core.dto.ExerciseRow;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseColumnResponse;
import com.nozma.core.dto.response.ExerciseRowResponse;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.nozma.core.entity.BaseDomain;
import com.nozma.core.entity.exercises.DisplayExerciseSetting;
import com.nozma.core.entity.exercises.ExerciseColumn;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.mapper.ExerciseMapper;
import com.nozma.core.mybatis.mapper.MybatisExerciseMapper;
import com.nozma.core.repository.DisplayExerciseSettingRepository;
import com.nozma.core.repository.ExerciseColumnRepository;
import com.nozma.core.service.ExerciseService;
import com.nozma.core.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    private final ExerciseMapper exerciseMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload) throws IllegalAccessException {
        // TODO:
        //  paging
        //  Exercises created by users
        
        Optional<DisplayExerciseSetting> displayExerciseSettings = displayExerciseSettingRepository
                .findOneByAccountIdAndCode(
                        SecurityUtil.getCurrentAccountId(), "exercises"
                );
        
        List<ExerciseColumn> columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        
        displayExerciseSettings = Optional.of(displayExerciseSettings.orElse(
                DisplayExerciseSetting
                        .builder()
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
                        .build()));
        
        // IF there are no changes, does it execute this code ??
        displayExerciseSettings = Optional.of(displayExerciseSettingRepository.save(displayExerciseSettings.get()));
        
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponses(displayExerciseSettings.get(),
                columns);
        
        List<String> visibleColumnCodes = exerciseColumnResponses
                .stream()
                .filter(ExerciseColumnResponse::visible)
                .map(ExerciseColumnResponse::code)
                .toList();
        
        List<ExerciseRow> exerciseRows = mybatisExerciseMapper.selectDynamicFields(
                visibleColumnCodes,
                exercisePagePayload,
                List.of(Constant.SYSTEM_AUDITOR, SecurityUtil.getCurrentAccountName())
        );
        
        int totalRowsCount = mybatisExerciseMapper.countExerciseRows(
                visibleColumnCodes,
                exercisePagePayload,
                List.of(Constant.SYSTEM_AUDITOR, SecurityUtil.getCurrentAccountName())
        );
        
        List<ExerciseRowResponse> exerciseRowResponses = exerciseRows
                .stream()
                .map(exerciseMapper::exerciseRowToResponse)
                .toList();
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponses(
            DisplayExerciseSetting displayExerciseSetting,
            List<ExerciseColumn> columns
    ) throws IllegalAccessException {
        
        try {
            List<ExerciseColumnResponse> exerciseColumnResponseList = new ArrayList<>();
            
            List<String> exclusiveFields = List.of(
                    DisplayExerciseSetting.Fields.id,
                    DisplayExerciseSetting.Fields.accountId,
                    DisplayExerciseSetting.Fields.code,
                    BaseDomain.Fields.status,
                    BaseDomain.Fields.createdDate,
                    BaseDomain.Fields.createdBy,
                    BaseDomain.Fields.updatedDate,
                    BaseDomain.Fields.updatedBy,
                    "serialVersionUID"
            );
            
            List<Field> fields = Arrays.stream(displayExerciseSetting.getClass().getDeclaredFields())
                    .filter(
                            field -> !exclusiveFields.contains(field.getName())
                    )
                    .toList();
            
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                
                // Get the field's name and value
                boolean value;
                
                value = (Boolean) field.get(displayExerciseSetting);
                
                ExerciseColumn exerciseColumn = columns
                        .stream()
                        .filter(
                                column -> column.getCode().equals(field.getName())
                        )
                        .findFirst()
                        .orElseThrow();
                
                exerciseColumnResponseList.add(
                        new ExerciseColumnResponse(
                                field.getName(),
                                exerciseColumn.getName(),
                                exerciseColumn.getType(),
                                value)
                );
            }
            return exerciseColumnResponseList;
            
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }
    
    @Override
    public ExerciseTableResponse getExercisesForGuest(ExercisePagePayload exercisePagePayload) {
        List<ExerciseColumn> columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        
        List<String> visibleColumnCodes = columns
                .stream()
                .map(ExerciseColumn::getCode)
                .toList();
        
        List<ExerciseRow> exerciseRows = mybatisExerciseMapper
                .selectDynamicFields(
                        visibleColumnCodes,
                        exercisePagePayload,
                        Collections.singletonList(Constant.SYSTEM_AUDITOR)
                );
        
        int totalRowsCount = mybatisExerciseMapper
                .countExerciseRows(
                        visibleColumnCodes,
                        exercisePagePayload,
                        Collections.singletonList(Constant.SYSTEM_AUDITOR)
                );
        
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponsesForGuest(columns);
        
        List<ExerciseRowResponse> exerciseRowResponses = exerciseRows
                .stream()
                .map(exerciseMapper::exerciseRowToResponse)
                .toList();
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponsesForGuest(List<ExerciseColumn> columns) {
        return columns
                .stream()
                .map(
                        column ->
                                new ExerciseColumnResponse(
                                        column.getCode(), column.getName(), column.getType()
                                )
                )
                .toList();
    }
}
