package com.ecommerce.userservice.exception;

import com.ecommerce.userservice.dto.response.ApiResponse;
import com.ecommerce.userservice.util.ResponseEntityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException exception){
        return ResponseEntityUtil.createFailureResponse(exception.getMessage());
    }
}
