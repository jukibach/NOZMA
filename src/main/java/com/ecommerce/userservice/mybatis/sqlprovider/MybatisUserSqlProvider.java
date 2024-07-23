package com.ecommerce.userservice.mybatis.sqlprovider;

import com.ecommerce.userservice.constant.Constant;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.stream.Collectors;

public class MybatisUserSqlProvider {
    
    // Define the mapping between fields and SQL columns
    private static final Map<String, String> FIELD_TO_COLUMN_MAP = Map.ofEntries(
            // Fields from users table
            Map.entry("accountName", "accounts.account_name"),
            Map.entry("password", "accounts.password"),
            Map.entry("email", "accounts.email"),
            Map.entry("isLocked", "accounts.is_locked"),
            Map.entry("creationTime", "accounts.created_date")
    );
    
    public String selectFields(PagePayload pagePayload) {
        var sql = new SQL();
        
        pagePayload.visibleColumns().forEach(field ->
                sql.SELECT(FIELD_TO_COLUMN_MAP.get(field) + Constant.AS + field));
        
        var accountSchemaName = CommonUtil.getTableNameAndSchema(Account.class)[0];
        var accountTableName = CommonUtil.getTableNameAndSchema(Account.class)[1];
        var accountEntityName = CommonUtil.getEntityName(Account.class);
        
        var baseTable = accountSchemaName.concat(Constant.DOT).concat(accountTableName)
                .concat(Constant.AS).concat(accountEntityName);
        
        sql.FROM(baseTable);
        
        // Add sorting
        if (CommonUtil.isNonNullOrNonEmpty(pagePayload.sortPreferences())) {
            var sortClause = convertMapToOrderByClause(pagePayload.sortPreferences());
            sql.ORDER_BY(sortClause);
        }
        
        // Add conditions
        if (CommonUtil.isNonNullOrNonEmpty(pagePayload.searchName())) {
            String whereClause = FIELD_TO_COLUMN_MAP.keySet().stream()
                    .filter(field -> pagePayload.visibleColumns().contains(field))
                    .map(field -> field.concat(Constant.LIKE).concat(pagePayload.searchName()))
                    .collect(Collectors.joining(Constant.OR));
            sql.WHERE(whereClause);
        }
        
        sql.LIMIT(pagePayload.pageSize()).OFFSET(pagePayload.pageIndex());
        
        return sql.toString();
    }
    
    public String convertMapToOrderByClause(Map<String, String> fieldsMap) {
        // Build the SQL ORDER BY clause
        return fieldsMap.entrySet().stream()
                .map(entry -> FIELD_TO_COLUMN_MAP.get(entry.getKey()) + Constant.SPACE + entry.getValue()) // Map field name to SQL column and sort order
                .collect(Collectors.joining(Constant.COMMA));
    }
}
