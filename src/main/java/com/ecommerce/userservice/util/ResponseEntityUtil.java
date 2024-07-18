package com.ecommerce.userservice.util;

import com.ecommerce.userservice.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {
    private ResponseEntityUtil() {
    }
    
    public static ResponseEntity<ApiResponse> createSuccessResponse(Object data) {
        return new ResponseEntity<>(ApiResponse.ok("Request was successful.", data), HttpStatus.OK);
    }
    
    public static ResponseEntity<ApiResponse> createSuccessResponseWithoutData() {
        return new ResponseEntity<>(ApiResponse.ok("Request was successful.", null), HttpStatus.OK);
    }
    
    public static ResponseEntity<ApiResponse> createFailureResponse(String message) {
        return new ResponseEntity<>(ApiResponse.badRequest(message, null), HttpStatus.BAD_REQUEST);
    }
}
