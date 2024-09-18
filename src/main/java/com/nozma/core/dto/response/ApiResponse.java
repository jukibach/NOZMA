package com.nozma.core.dto.response;

import com.nozma.core.constant.MessageId;
import com.nozma.core.util.DateUtil;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResponse(
        HttpStatus status,
        String code,
        String message,
        Object result,
        String timeStamp
) {
    public static ApiResponse ok(String message, Object result) {
        return new ApiResponse(HttpStatus.OK, MessageId.OK, message, result,
                DateUtil.convertLocalDateTimeToString(LocalDateTime.now()));
    }
    
    public static ApiResponse badRequest(String message, Object result, String code) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, code, message, result,
                DateUtil.convertLocalDateTimeToString(LocalDateTime.now()));
    }
    
    public static ApiResponse noContent(String message) {
        return new ApiResponse(HttpStatus.NO_CONTENT, MessageId.OK, message, null,
                DateUtil.convertLocalDateTimeToString(LocalDateTime.now()));
    }
}
