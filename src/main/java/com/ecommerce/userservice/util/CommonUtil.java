package com.ecommerce.userservice.util;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class CommonUtil {
    private CommonUtil() {
    }
    
    public static boolean isNullOrEmpty(Object object) {
        return ObjectUtils.isEmpty(object);
    }
    
    public static boolean isNonNullOrNonEmpty(Object object) {
        return !ObjectUtils.isEmpty(object);
    }
    
    public static String[] getTableNameAndSchema(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            String tableName = table.name();
            String schemaName = table.schema();
            return new String[]{schemaName, tableName};
        }
        return new String[0];
    }
    
    public static String getEntityName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            return entity.name();
        }
        return " ";
    }
    
    public static String combineString(String... objects) {
        if (isNullOrEmpty(objects)) {
            throw new IllegalArgumentException();
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        
        for (Object str : objects) {
            if (isNonNullOrNonEmpty(str)) {
                stringBuilder.append(str);
            }
        }
        
        return stringBuilder.toString();
    }
    
    public static String retrieveToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (CommonUtil.isNullOrEmpty(authHeader)) return null;
        return authHeader.replace("Bearer ", "");
    }
}
