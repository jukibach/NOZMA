package com.nozma.core.util;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.ObjectUtils;

public class CommonUtil {
    private CommonUtil() {
    }
    
    public static boolean isNullOrEmpty(Object object) {
        return ObjectUtils.isEmpty(object);
    }
    
    public static boolean isNonNullOrNonEmpty(Object object) {
        return !ObjectUtils.isEmpty(object);
    }
    
    public static String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            return table.name();
        }
        return Strings.EMPTY;
    }
    
    public static String getSchemaName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            return table.schema();
        }
        return Strings.EMPTY;
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
