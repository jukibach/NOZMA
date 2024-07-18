package com.ecommerce.userservice.util;

import org.springframework.util.ObjectUtils;

public class CommonUtil {
    private CommonUtil() {}
    
    public static boolean isNullOrEmpty(Object object) {
        return ObjectUtils.isEmpty(object);
    }
    
    public static boolean isNonNullOrNonEmpty(Object object) {
        return !ObjectUtils.isEmpty(object);
    }
}
