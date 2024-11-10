package com.nozma.core.service.exercises.impl;

import com.nozma.core.constant.Constant;
import com.nozma.core.dto.ExerciseRow;
import com.nozma.core.dto.request.ExercisePagePayload;
import com.nozma.core.dto.response.ExerciseColumnResponse;
import com.nozma.core.dto.response.ExerciseRowResponse;
import com.nozma.core.dto.response.ExerciseTableResponse;
import com.nozma.core.entity.exercises.ExerciseColumn;
import com.nozma.core.enums.RecordStatus;
import com.nozma.core.mapper.ExerciseMapper;
import com.nozma.core.mybatis.mapper.MybatisExerciseMapper;
import com.nozma.core.repository.ExerciseColumnRepository;
import com.nozma.core.service.exercises.GuestExerciseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GuestExerciseServiceImpl implements GuestExerciseService {
    private final ExerciseColumnRepository exerciseColumnRepository;
    private final MybatisExerciseMapper mybatisExerciseMapper;
    private final ExerciseMapper exerciseMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class
            , propagation = Propagation.REQUIRED
            , isolation = Isolation.READ_COMMITTED)
    public ExerciseTableResponse getExercises(ExercisePagePayload exercisePagePayload) {
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
        
        List<ExerciseColumnResponse> exerciseColumnResponses = setColumnResponses(columns);
        
        List<ExerciseRowResponse> exerciseRowResponses = exerciseRows
                .stream()
                .map(exerciseMapper::exerciseRowToResponse)
                .toList();
        
        return new ExerciseTableResponse(exerciseColumnResponses, exerciseRowResponses, totalRowsCount);
    }
    
    private List<ExerciseColumnResponse> setColumnResponses(List<ExerciseColumn> columns) {
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
