package com.nozma.core.aop;

import com.nozma.core.constant.ApiURL;
import com.nozma.core.dto.response.ApiResponse;
import com.nozma.core.entity.account.Account;
import com.nozma.core.entity.account.User;
import com.nozma.core.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
public class LoggingAspect {
    
    private static final List<String> EXCLUSIVE_REQUEST_BODY = List.of(ApiURL.LOGIN);
    
    private static final List<String> EXCLUSIVE_ENTITIES = List.of(Account.class.getName(), User.class.getName());
    
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
    }
    
    @Pointcut("""
                within(com.nozma.core.service.*.*.*) || within(com.nozma.core.repository.*.*.*)
            """)
    public void anyRepositoriesAndServices() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    
    @Pointcut("within(com.nozma.core.controller..*)")
    public void restControllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    
    @Pointcut("""
                within(com.nozma.core.service.*.*.CustomUserDetailServiceImpl))
            """)
    public void whatIDontWantToMatch() {
    
    }
    
    @Pointcut("""
                within(com.nozma.core.service.*.*.*Factory)
            """)
    public void anyFactory() {
    
    }
    
    @Around(value = "anyFactory()")
    public Object inspectAnyFactory(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        
        String method = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        
        if (log.isDebugEnabled()) {
            log.debug("""
                            
                            -------------ENTER METHOD-----------------
                                Method: {}()
                            -------------------------------------------
                            """
                    , method
            );
        }
        
        try {
            Object result = joinPoint.proceed();
            
            if (log.isDebugEnabled()) {
                log.debug("""
                                
                                -------------EXIT METHOD-----------------
                                    Method: {}()
                                    Process time: {} ms
                                -----------------------------------------
                                """
                        , joinPoint.getSignature().getName()
                        , System.currentTimeMillis() - startTime
                );
            }
            
            return result;
            
        } catch (Throwable e) {
            log.error("""
                            
                            -------------THROW EXCEPTION-----------------
                                Process time: {} ms
                                Illegal argument: {}()
                            ------------------------------------------
                            """
                    , System.currentTimeMillis() - startTime
                    , joinPoint.getSignature().getName());
            throw e;
        }
    }
    
    @Around(value = "restControllerPointcut()")
    public Object inspectRequestBody(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        
        long startTime = System.currentTimeMillis();
        
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
        
        boolean isExclusiveRequest = EXCLUSIVE_REQUEST_BODY
                .stream()
                .anyMatch(
                        exclusiveRequestURL -> httpServletRequest.getRequestURI().contains(exclusiveRequestURL)
                );
        
        String json = isExclusiveRequest
                ? "Sensitive request body"
                : "Request body: %s".formatted(JsonUtils.convertToJson(joinPoint.getArgs()));
        log.info("""
                        
                        -------------REQUEST INFO-----------------
                            HTTP Method: {}
                            Request URI: {}
                            IP address: {}
                            Query: {}
                            Controller method: {}()
                            {}
                        -------------------------------------------
                        """
                , httpServletRequest.getMethod()
                , httpServletRequest.getRequestURI()
                , httpServletRequest.getRemoteAddr()
                , httpServletRequest.getQueryString()
                , joinPoint.getSignature().getName()
                , json
        );
        
        Object result = joinPoint.proceed();
        
        log.info("""
                        
                        -------------FINISHED REQUEST-----------------
                            Request URI: {}
                            Method: {}()
                            Process time: {} ms
                        -----------------------------------------
                        """
                , httpServletRequest.getRequestURI()
                , joinPoint.getSignature().getName()
                , System.currentTimeMillis() - startTime
        );
        
        return result;
        
    }
    
    @AfterReturning(value = "within(com.nozma.core.exception..*)", returning = "result")
    public void inspectErrorResponseBody(JoinPoint joinPoint, ResponseEntity<ApiResponse> result) {
        Logger log = logger(joinPoint);
        
        if (log.isDebugEnabled() && !HttpStatus.OK.equals(Objects.requireNonNull(result.getBody()).status())) {
            
            log.debug("""
                            
                            -------------ERROR API RESULT-----------------
                                Exit: {}() with and status = {}
                                Response body: {}
                            ---------------------------------------------------
                            """
                    , joinPoint.getSignature().getName()
                    , result.getStatusCode()
                    , JsonUtils.convertToJson(result.getBody())
            );
        }
    }
    
    /**
     * Advice that logs surrounding the entire method.
     *
     * @param joinPoint join point for advice.
     */
    @Around(value = "anyRepositoriesAndServices() && !whatIDontWantToMatch() && !anyFactory()")
    public Object anyMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        String method = joinPoint.getSignature().getName();
        boolean isValidatingToken = method.toLowerCase().contains("token");
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        
        boolean isExclusiveRequest = EXCLUSIVE_REQUEST_BODY
                .stream()
                .anyMatch(
                        exclusiveRequestURL -> httpServletRequest.getRequestURI().contains(exclusiveRequestURL)
                );
        
        String arguments = isExclusiveRequest || isValidatingToken
                ? "[Sensitive arguments]"
                : JsonUtils.convertToJson(joinPoint.getArgs());
        
        long startTime = System.currentTimeMillis();
        
        if (log.isDebugEnabled()) {
            log.debug("""
                            
                            -------------ENTER METHOD-----------------
                                Method: {}()
                            -------------------------------------------
                            """
                    , method
            );
        }
        
        try {
            Object result = joinPoint.proceed();
            
            if (log.isDebugEnabled()) {
                log.debug("""
                                
                                -------------EXIT METHOD-----------------
                                    Method: {}()
                                    Process time: {} ms
                                -----------------------------------------
                                """
                        , joinPoint.getSignature().getName()
                        , System.currentTimeMillis() - startTime
                );
            }
            
            return result;
            
        } catch (Throwable e) {
            log.error("""
                            
                            -------------THROW EXCEPTION-----------------
                                Process time: {} ms
                                Illegal argument: {} in {}()
                            ------------------------------------------
                            """
                    , System.currentTimeMillis() - startTime
                    , arguments
                    , joinPoint.getSignature().getName());
            throw e;
        }
    }
}
