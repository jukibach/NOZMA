package com.ecommerce.userservice.util;

import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.exception.BusinessException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {
    private ResponseEntityUtil() {
    }
    
    public static ResponseEntity<ApiResponse> createSuccessfulOkResponse(Object data) {
        return new ResponseEntity<>(ApiResponse.ok("Request was successful.", data), HttpStatus.OK);
    }
    
    public static ResponseEntity<ApiResponse> createSuccessfulPatchResponse() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    public static ResponseEntity<ApiResponse> createSuccessResponseWithCreatedStatus(Object data) {
        return new ResponseEntity<>(ApiResponse.ok("New resource was created", data), HttpStatus.CREATED);
    }
    
    public static ResponseEntity<ApiResponse> createSuccessResponseWithoutData() {
        return new ResponseEntity<>(ApiResponse.ok("Request was successful.", null), HttpStatus.OK);
    }
    
    public static ResponseEntity<ApiResponse> createFailureResponse(BusinessException exception,
                                                                    MessageSource messageSource) {
        String message = messageSource.getMessage(exception.getMessage(), null,
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
    
    public static ResponseEntity<ApiResponse> createFailureResponseForInvalidFields(BusinessException exception,
                                                                    MessageSource messageSource) {
        String message = messageSource.getMessage(exception.getMessage(), new Object[]{exception.getResult().toString()},
                LocaleContextHolder.getLocale());
        return new ResponseEntity<>(ApiResponse.badRequest(message, null, exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
