package com.ecommerce.userservice.mybatis.mapper;

import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.dto.response.ExerciseRowResponse;
import com.ecommerce.userservice.mybatis.sqlprovider.MybatisExerciseSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface MybatisExerciseMapper {
    @SelectProvider(type = MybatisExerciseSqlProvider.class, method = "selectFields")
    List<ExerciseRowResponse> selectFields(@Param("exerciseColumnNames") List<String> exerciseColumnNames,
                                           @Param("exercisePagePayload") ExercisePagePayload exercisePagePayload);
}
