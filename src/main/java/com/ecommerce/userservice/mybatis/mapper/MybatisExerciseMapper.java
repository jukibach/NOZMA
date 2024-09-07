package com.ecommerce.userservice.mybatis.mapper;

import com.ecommerce.userservice.dto.ExerciseRow;
import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.mybatis.sqlprovider.MybatisExerciseSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface MybatisExerciseMapper {
    @SelectProvider(type = MybatisExerciseSqlProvider.class, method = "selectFields")
    List<ExerciseRow> selectFields(@Param("exerciseColumnNames") List<String> exerciseColumnNames,
                                   @Param("exercisePagePayload") ExercisePagePayload exercisePagePayload);
    
    @SelectProvider(type = MybatisExerciseSqlProvider.class, method = "countExerciseRows")
    Integer countExerciseRows(@Param("exerciseColumnNames") List<String> exerciseColumnNames,
                                   @Param("exercisePagePayload") ExercisePagePayload exercisePagePayload);
}
