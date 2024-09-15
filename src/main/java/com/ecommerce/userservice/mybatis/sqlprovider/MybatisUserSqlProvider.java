package com.ecommerce.userservice.mybatis.sqlprovider;

import com.ecommerce.userservice.constant.Constant;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.AccountDetailResponse;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MybatisUserSqlProvider {
    
    // Define the mapping between fields and SQL columns
    private static final Map<String, String> FIELD_TO_COLUMN_MAP = Map.ofEntries(
            // Fields from users table
            Map.entry(AccountDetailResponse.Fields.accountId, "accounts.id"),
            Map.entry(AccountDetailResponse.Fields.accountName, "accounts.account_name"),
            Map.entry(AccountDetailResponse.Fields.email, "accounts.email"),
            Map.entry(AccountDetailResponse.Fields.isLocked, "accounts.is_locked"),
            Map.entry(AccountDetailResponse.Fields.creationTime, "accounts.created_date")
    );
    
    private static final Map<String, String> ACCOUNT_DETAIL_RESPONSE = Map.ofEntries(
            Map.entry(AccountDetailResponse.Fields.accountId, "accounts.id"),
            Map.entry(AccountDetailResponse.Fields.accountName, "accounts.account_name"),
            Map.entry(AccountDetailResponse.Fields.email, "accounts.email"),
            Map.entry(AccountDetailResponse.Fields.firstName, "users.first_name"),
            Map.entry(AccountDetailResponse.Fields.lastName, "users.last_name"),
            Map.entry(AccountDetailResponse.Fields.creationTime, "accounts.created_date"),
            Map.entry(AccountDetailResponse.Fields.status, "accounts.status"),
            Map.entry(AccountDetailResponse.Fields.isLocked, "accounts.is_locked")
    );
    
    public String selectFields(PagePayload pagePayload) {
        var sql = new SQL();
        
        pagePayload.visibleColumns().forEach(field ->
                sql.SELECT("%s AS %s".formatted(ACCOUNT_DETAIL_RESPONSE.get(field), field)));
        var accountSchemaName = CommonUtil.getSchemaName(Account.class);
        var accountTableName = CommonUtil.getTableName(Account.class);
        var accountEntityName = CommonUtil.getEntityName(Account.class);
        
        // s_account.t_accounts as accounts
        var baseTable = """
                %s.%s AS %s
                """.formatted(accountSchemaName, accountTableName, accountEntityName);
        
        sql.FROM(baseTable);
        
        // Add sorting
        if (CommonUtil.isNonNullOrNonEmpty(pagePayload.sortPreferences())) {
            var sortClause = convertMapToOrderByClause(pagePayload.sortPreferences());
            sql.ORDER_BY(sortClause);
        }
        
        // Add conditions
        if (CommonUtil.isNonNullOrNonEmpty(pagePayload.searchName())) {
//            String whereClause = ACCOUNT_DETAIL_RESPONSE.keySet().stream()
//                    .filter(field -> pagePayload.visibleColumns().contains(field) )
//                    .map(field -> """
//                            %s LIKE '%%%s%%'
//                            """.formatted(ACCOUNT_DETAIL_RESPONSE.get(field), pagePayload.searchName()))
//                    .collect(Collectors.joining(Constant.OR));
//
            List<String> conditions = new ArrayList<>();
            ACCOUNT_DETAIL_RESPONSE.forEach((responseColumn, databaseField) -> {
                if (pagePayload.visibleColumns().contains(responseColumn)
                        && !responseColumn.equals(AccountDetailResponse.Fields.creationTime)) {
                    conditions.add("""
                            %s ILIKE '%%%s%%'
                            """.formatted(databaseField, pagePayload.searchName().trim().toLowerCase()));
                }
            });
            String whereClause = String.join(Constant.OR, conditions);
            sql.WHERE(whereClause);
        }
        
        sql.LIMIT(pagePayload.pageSize()).OFFSET(pagePayload.pageIndex());
        
        return sql.toString();
    }
    
    private String convertMapToOrderByClause(Map<String, String> fieldsMap) {
        // Build the SQL ORDER BY clause
        return fieldsMap.entrySet().stream()
                .map(entry -> "%s %s".formatted(FIELD_TO_COLUMN_MAP.get(entry.getKey()), entry.getValue())) // Map field name to SQL column and sort order
                .collect(Collectors.joining(Constant.COMMA));
    }
    
    public String getAccountDetail(Long accountId) {
        var sql = new SQL();
        var accountSchemaName = CommonUtil.getSchemaName(Account.class);
        var accountTableName = CommonUtil.getTableName(Account.class);
        var accountEntityName = CommonUtil.getEntityName(Account.class);
        
        var userTableName = CommonUtil.getTableName(User.class);
        var userEntityName = CommonUtil.getEntityName(User.class);
        
        // s_account.t_accounts as accounts
        var baseTable = """
                    %s.%s AS %s
                """.formatted(accountSchemaName, accountTableName, accountEntityName);
        
        var joinTable = """
                %s.%s AS %s ON %s.user_id = %s.id
                """.formatted(accountSchemaName, userTableName, userEntityName, accountEntityName, userEntityName);
        
        ACCOUNT_DETAIL_RESPONSE.forEach((field, column) -> sql.SELECT("%s AS %s".formatted(column, field)));
        
        sql.FROM(baseTable).JOIN(joinTable);
        
        String whereClause = """
                %s.id = %d
                """.formatted(accountEntityName, accountId);
        sql.WHERE(whereClause);
        
        return sql.toString();
    }
}
