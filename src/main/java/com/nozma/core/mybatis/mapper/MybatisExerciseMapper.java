package com.nozma.core.mybatis.mapper;

import com.nozma.core.dto.ExerciseRow;
import com.nozma.core.dto.request.ExercisePagePayload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MybatisExerciseMapper {
    List<ExerciseRow> selectDynamicFields(@Param("exerciseColumnNames") List<String> exerciseColumnNames,
                                          @Param("exercisePagePayload") ExercisePagePayload exercisePagePayload,
                                          @Param("createdBys") List<String> createdBys);
    
    Integer countExerciseRows(@Param("exerciseColumnNames") List<String> exerciseColumnNames,
                              @Param("exercisePagePayload") ExercisePagePayload exercisePagePayload,
                              @Param("createdBys") List<String> createdBys);
}
