package com.ecommerce.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This method is called when a user tries to access a secured REST resource without supplying any credentials
        
        // Send a 401 Unauthorized response because the user is not authenticated
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}