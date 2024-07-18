package com.ecommerce.userservice.dto.response;

import com.ecommerce.userservice.util.DateUtil;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResponse(
        HttpStatus status,
        String message,
        Object result,
        String timeStamp
) {
    public static ApiResponse ok(String message, Object result) {
        return new ApiResponse(HttpStatus.OK, message, result,
                DateUtil.convertLocalDateTimeToString(LocalDateTime.now()));
    }
    
    public static ApiResponse badRequest(String message, Object result) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, message, result,
                DateUtil.convertLocalDateTimeToString(LocalDateTime.now()));
    }
}
