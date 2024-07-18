package com.ecommerce.userservice.config;

import com.ecommerce.userservice.service.AccountService;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.util.JwtUtil;
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

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = retrieveToken(request);
        if (CommonUtil.isNonNullOrNonEmpty(token)) {
            var accountName = jwtUtil.validateToken(token);
            var account = accountService.findByAccountName(accountName);
            var authentication = new UsernamePasswordAuthenticationToken(account, null,null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    
    private String retrieveToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (CommonUtil.isNullOrEmpty(authHeader)) return null;
        return authHeader.replace("Bearer ", "");
    }
}
