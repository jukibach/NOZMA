package com.ecommerce.userservice.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = jwtUtil.retrieveToken(request);
        try {
            if (CommonUtil.isNonNullOrNonEmpty(token)) {
                var accountName = jwtUtil.validateToken(token);
                var account = accountService.findByAccountName(accountName);
                var authentication = new UsernamePasswordAuthenticationToken(account, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException ex) {
            request.setAttribute("expired", ex.getMessage());
            // Custom error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            errorResponse.put("error", "Unauthorized");
            // Write the custom error message to the response
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
        filterChain.doFilter(request, response);
    }
  
}
