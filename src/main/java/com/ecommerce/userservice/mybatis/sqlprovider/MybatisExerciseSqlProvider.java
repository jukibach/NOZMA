package com.ecommerce.userservice.mybatis.sqlprovider;

import com.ecommerce.userservice.constant.Constant;
import com.ecommerce.userservice.dto.request.ExercisePagePayload;
import com.ecommerce.userservice.entity.BaseDomain;
import com.ecommerce.userservice.entity.Exercise;
import com.ecommerce.userservice.enums.RecordStatus;
import com.ecommerce.userservice.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MybatisExerciseSqlProvider {
    
    // Define the mapping between fields and SQL columns
    private static final Map<String, String> FIELD_TO_COLUMN_MAP = Map.ofEntries(
            // Fields from users table
            Map.entry(Exercise.Fields.id, "id"),
            Map.entry(Exercise.Fields.name, "name"),
            Map.entry(Exercise.Fields.description, "description"),
            Map.entry(Exercise.Fields.majorMuscle, "major_muscle"),
            Map.entry(Exercise.Fields.mechanics, "mechanics"),
            Map.entry(Exercise.Fields.bodyRegion, "body_region"),
            Map.entry(Exercise.Fields.laterality, "laterality"),
            Map.entry(Exercise.Fields.isWeight, "is_weight"),
            Map.entry(Exercise.Fields.isCardio, "is_cardio"),
            Map.entry(Exercise.Fields.isPlyo, "is_plyo")
    );
    
    public String selectFields(List<String> exerciseColumnNames, ExercisePagePayload exercisePagePayload) {
        var sql = new SQL();
        sql.SELECT(Exercise.Fields.id);
        exerciseColumnNames.forEach(field -> sql.SELECT("%s AS %s".formatted(FIELD_TO_COLUMN_MAP.get(field), field)));
        
        var workoutSchemaName = CommonUtil.getSchemaName(Exercise.class);
        var exerciseTableName = CommonUtil.getTableName(Exercise.class);
        var exerciseEntityName = CommonUtil.getEntityName(Exercise.class);
        
        // s_account.t_accounts as accounts
        var baseTable = "%s.%s AS %s".formatted(workoutSchemaName, exerciseTableName, exerciseEntityName);
//        sql.SELECT("SELECT COUNT(*) as totalRowCount FROM %s ".formatted(baseTable));
        sql.FROM(baseTable);
        
        // Add sorting
        if (CommonUtil.isNonNullOrNonEmpty(exercisePagePayload.sortPreferences())) {
            var sortClause = convertMapToOrderByClause(exercisePagePayload.sortPreferences());
            sql.ORDER_BY(sortClause);
        } else {
            String orderBy = "%s ASC, %s DESC".formatted(Exercise.Fields.id, "created_date");
            sql.ORDER_BY(orderBy);
        }
        
        sql.WHERE("%s = '%s'".formatted(BaseDomain.Fields.status, RecordStatus.ACTIVE.name()))
                .AND()
                .WHERE("created_by = '%s'".formatted("SYSTEM"));
//        .concat(Constant.OR)
//                .concat(BaseDomain.Fields.createdBy.concat(Constant.EQUAL).concat(createdByUser))
        // Add conditions
        if (CommonUtil.isNonNullOrNonEmpty(exercisePagePayload.searchName())) {
            List<String> conditions = new ArrayList<>();
            FIELD_TO_COLUMN_MAP.forEach((responseColumn, databaseField) -> {
                if (exerciseColumnNames.contains(responseColumn)
                        && !List.of(Exercise.Fields.isPlyo, Exercise.Fields.isCardio, Exercise.Fields.isWeight).contains(responseColumn)) {
                    conditions.add("""
                            %s ILIKE '%%%s%%'
                            """.formatted(databaseField, exercisePagePayload.searchName()));
                }
            });
            String whereClause = String.join(Constant.OR, conditions);
            sql.WHERE(whereClause);
        }
        
        sql.LIMIT(
                CommonUtil.isNonNullOrNonEmpty(exercisePagePayload.pageSize()) ? exercisePagePayload.pageSize() : 20
        ).OFFSET(
                CommonUtil.isNonNullOrNonEmpty(exercisePagePayload.pageIndex()) ? (long) exercisePagePayload.pageIndex() * exercisePagePayload.pageSize() : 0
        );
        
        return sql.toString();
    }
    
    public String countExerciseRows(List<String> exerciseColumnNames, ExercisePagePayload exercisePagePayload) {
        var sql = new SQL();
        var workoutSchemaName = CommonUtil.getSchemaName(Exercise.class);
        var exerciseTableName = CommonUtil.getTableName(Exercise.class);
        var exerciseEntityName = CommonUtil.getEntityName(Exercise.class);
        
        // s_account.t_accounts as accounts
        var baseTable = "%s.%s AS %s".formatted(workoutSchemaName, exerciseTableName, exerciseEntityName);
        
        sql.SELECT("COUNT(*)");
        
        sql.FROM(baseTable);
        
        sql.WHERE("%s = '%s'".formatted(BaseDomain.Fields.status, RecordStatus.ACTIVE.name()))
                .AND()
                .WHERE("created_by = '%s'".formatted("SYSTEM"));
//        .concat(Constant.OR)
//                .concat(BaseDomain.Fields.createdBy.concat(Constant.EQUAL).concat(createdByUser))
        // Add conditions
        if (CommonUtil.isNonNullOrNonEmpty(exercisePagePayload.searchName())) {
            List<String> conditions = new ArrayList<>();
            FIELD_TO_COLUMN_MAP.forEach((responseColumn, databaseField) -> {
                if (exerciseColumnNames.contains(responseColumn)
                        && !List.of(Exercise.Fields.isPlyo, Exercise.Fields.isCardio, Exercise.Fields.isWeight).contains(responseColumn)) {
                    conditions.add("""
                            %s LIKE '%%%s%%'
                            """.formatted(databaseField, exercisePagePayload.searchName()));
                }
            });
            String whereClause = String.join(Constant.OR, conditions);
            sql.WHERE(whereClause);
        }
        
        return sql.toString();
    }
    
    private String convertMapToOrderByClause(Map<String, String> fieldsMap) {
        // Build the SQL ORDER BY clause
        return fieldsMap.entrySet().stream()
                .map(entry -> "%s %s".formatted(FIELD_TO_COLUMN_MAP.get(entry.getKey()), entry.getValue())) // Map field name to SQL column and sort order
                .collect(Collectors.joining(Constant.COMMA));
    }
}
