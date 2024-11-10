package com.nozma.core.service.exercises.impl;

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
import com.nozma.core.service.exercises.NormalUserExerciseService;
import com.nozma.core.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NormalUserExerciseServiceImpl implements NormalUserExerciseService {
    static final List<String> EXCLUSIVE_FIELDS = List.of(
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
    
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final DisplayExerciseSettingRepository displayExerciseSettingRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    private final ExerciseMapper exerciseMapper;
    
    @Autowired
    public NormalUserExerciseServiceImpl(
            ExerciseColumnRepository exerciseColumnRepository
            , DisplayExerciseSettingRepository displayExerciseSettingRepository
            , MybatisExerciseMapper mybatisExerciseMapper
            , ExerciseMapper exerciseMapper
    ) {
        this.exerciseColumnRepository = exerciseColumnRepository;
        this.displayExerciseSettingRepository = displayExerciseSettingRepository;
        this.mybatisExerciseMapper = mybatisExerciseMapper;
        this.exerciseMapper = exerciseMapper;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class
            , propagation = Propagation.REQUIRED
            , isolation = Isolation.READ_COMMITTED)
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload) throws IllegalAccessException {
        
        DisplayExerciseSetting displayExerciseSettings = displayExerciseSettingRepository
                .findOneByAccountIdAndCode(
                        SecurityUtil.getCurrentAccountId(), "exercises"
                ).orElse(
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
                                .build()
                );
        
        displayExerciseSettings = displayExerciseSettingRepository.save(displayExerciseSettings);
        
        List<ExerciseColumn> columns = exerciseColumnRepository.findAllByStatus(RecordStatus.ACTIVE);
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponses(displayExerciseSettings,
                columns);
        
        List<String> visibleColumnCodes = exerciseColumnResponses
                .stream()
                .filter(ExerciseColumnResponse::visible)
                .map(ExerciseColumnResponse::code)
                .toList();
        
        List<ExerciseRow> exerciseRows = mybatisExerciseMapper.selectDynamicFields(
                visibleColumnCodes,
                exercisePagePayload,
                List.of(Constant.SYSTEM_AUDITOR,
                        Objects.requireNonNull(SecurityUtil.getCurrentAccountName().orElse(null)))
        );
        
        int totalRowsCount = mybatisExerciseMapper.countExerciseRows(
                visibleColumnCodes,
                exercisePagePayload,
                List.of(Constant.SYSTEM_AUDITOR, SecurityUtil.getCurrentAccountName().orElse(null))
        );
        
        List<ExerciseRowResponse> exerciseRowResponses = exerciseRows
                .stream()
                .map(exerciseMapper::exerciseRowToResponse)
                .toList();
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    @Override
    public void deleteCustomExercise() {
        // TODO document why this method is empty
    }
    
    @Override
    public void addCustomExercise() {
        // TODO document why this method is empty
    }
    
    private List<ExerciseColumnResponse> setColumnResponses(
            DisplayExerciseSetting displayExerciseSetting,
            List<ExerciseColumn> columns
    ) throws IllegalAccessException {
        
        try {
            List<ExerciseColumnResponse> exerciseColumnResponseList = new ArrayList<>();
            
            List<Field> fields = Arrays.stream(displayExerciseSetting.getClass().getDeclaredFields())
                    .filter(field -> !EXCLUSIVE_FIELDS.contains(field.getName()))
                    .toList();
            
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                
                // Get the field's name and value
                boolean value = (Boolean) field.get(displayExerciseSetting);
                
                ExerciseColumn exerciseColumn = columns
                        .stream()
                        .filter(column -> column.getCode().equals(field.getName()))
                        .findFirst()
                        .orElseThrow();
                
                exerciseColumnResponseList.add(new ExerciseColumnResponse(
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
}
