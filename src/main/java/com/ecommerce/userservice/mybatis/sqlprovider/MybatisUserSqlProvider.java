package com.ecommerce.userservice.mybatis.sqlprovider;

import com.ecommerce.userservice.constant.Constant;
import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.AccountDetailResponse;
import com.ecommerce.userservice.entity.Account;
import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.util.CommonUtil;
import org.apache.ibatis.jdbc.SQL;

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
                sql.SELECT(FIELD_TO_COLUMN_MAP.get(field) + Constant.AS + field));
        var accountSchemaName = CommonUtil.getSchemaName(Account.class);
        var accountTableName = CommonUtil.getTableName(Account.class);
        var accountEntityName = CommonUtil.getEntityName(Account.class);
        
        // s_account.t_accounts as accounts
        var baseTable = accountSchemaName
                .concat(Constant.DOT)
                .concat(accountTableName)
                .concat(Constant.AS)
                .concat(accountEntityName);
        
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
    
    private String convertMapToOrderByClause(Map<String, String> fieldsMap) {
        // Build the SQL ORDER BY clause
        return fieldsMap.entrySet().stream()
                .map(entry -> FIELD_TO_COLUMN_MAP.get(entry.getKey()) + Constant.SPACE + entry.getValue()) // Map field name to SQL column and sort order
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
        var baseTable = accountSchemaName
                .concat(Constant.DOT)
                .concat(accountTableName)
                .concat(Constant.AS)
                .concat(accountEntityName);
        
        var joinTable = accountSchemaName.concat(Constant.DOT).concat(userTableName)
                .concat(Constant.AS).concat(userEntityName).concat(Constant.ON)
                
                .concat(accountEntityName).concat(Constant.DOT).concat("user_id")
                
                .concat(Constant.EQUAL)
                
                .concat(userEntityName).concat(Constant.DOT).concat("id");
        
        ACCOUNT_DETAIL_RESPONSE.forEach((field, column) -> sql.SELECT(column + Constant.AS + field));
        
        sql.FROM(baseTable).JOIN(joinTable);
        
        String whereClause = accountEntityName.concat(Constant.DOT).concat("id").concat(Constant.EQUAL).concat(String.valueOf(accountId));
        sql.WHERE(whereClause);
        
        return sql.toString();
    }
}
