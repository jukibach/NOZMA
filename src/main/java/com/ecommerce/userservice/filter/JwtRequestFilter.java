package com.ecommerce.userservice.filter;

import com.ecommerce.userservice.entity.TokenDetail;
import com.ecommerce.userservice.enums.StatusAndMessage;
import com.ecommerce.userservice.exception.BusinessException;
import com.ecommerce.userservice.service.TokenService;
import com.ecommerce.userservice.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final MessageSource messageSource;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        var profileToken = CommonUtil.retrieveToken(request);
        try {
            if (CommonUtil.isNonNullOrNonEmpty(profileToken)) {
                if (tokenService.checkTokenExistsInBlackList(profileToken)) {
                    throw new BusinessException(StatusAndMessage.TOKEN_EXPIRED);
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
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            String requestLanguage = request.getParameter("lang");
            if (CommonUtil.isNullOrEmpty(requestLanguage)) {
                requestLanguage = Locale.FRENCH.getLanguage();
            }
            LocaleContextHolder.setLocale(new Locale(requestLanguage));
            if (ex instanceof BusinessException) {
                handleBusinessException(response, ex);
            } else {
                handleNormalException(response, ex);
            }
        }
    }
    
    private void handleNormalException(HttpServletResponse response, Exception ex) throws IOException {
        // Custom error message in JSON format
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.name());
        log.error(ex.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // Write the custom error message to the response
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    
    private void handleBusinessException(HttpServletResponse response, Exception ex) throws IOException {
        // Custom error message in JSON format
        Map<String, String> errorResponse = new HashMap<>();
        String message = messageSource.getMessage(ex.getMessage(), null,
                LocaleContextHolder.getLocale());
        errorResponse.put("code", ex.getMessage());
        errorResponse.put("message", message);
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.name());
        log.error(message);
        // Write the custom error message to the response
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
