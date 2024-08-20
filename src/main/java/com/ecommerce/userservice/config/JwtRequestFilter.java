package com.ecommerce.userservice.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.service.TokenService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var profileToken = CommonUtil.retrieveToken(request);
        try {
            if (CommonUtil.isNonNullOrNonEmpty(profileToken)) {
                if (tokenService.checkTokenExistsInBlackList(profileToken)) {
                    throw new TokenExpiredException("Token expired", null); // Support multiple language
                }
                
                TokenDetail tokenDetail = tokenService.validateToken(profileToken);
                if (CommonUtil.isNonNullOrNonEmpty(tokenDetail.getAccountName())
                        && CommonUtil.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication())) {
                    var userDetails = userDetailsService.loadUserByUsername(tokenDetail.getAccountName());
                    
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        } catch (Exception ex) {
            String requestLanguage = request.getParameter("lang");
            if (CommonUtil.isNullOrEmpty(requestLanguage)) {
                requestLanguage = Locale.JAPANESE.getLanguage();
            }
            LocaleContextHolder.setLocale(new Locale(requestLanguage));
            if (ex instanceof TokenExpiredException) {
                request.setAttribute("expired", ex.getMessage());
                // Custom error message in JSON format
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", ex.getMessage());
                errorResponse.put("error", "Unauthorized");
                // Write the custom error message to the response
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                log.error("Token expired");
                
            }
            
            if (ex instanceof JWTVerificationException) {
                // Custom error message in JSON format
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", ex.getMessage());
                errorResponse.put("error", "Unauthorized");
                // Write the custom error message to the response
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                log.error("Token is invalid");
            }
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
