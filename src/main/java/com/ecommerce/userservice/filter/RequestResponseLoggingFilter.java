package com.ecommerce.userservice.filter;

import com.ecommerce.userservice.constant.ApiURL;
import com.ecommerce.userservice.util.CommonUtil;
import com.ecommerce.userservice.wrapper.RequestWrapper;
import com.ecommerce.userservice.wrapper.ResponseWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class RequestResponseLoggingFilter implements Filter {
    
    private final List<String> exclusiveRequestBody = List.of(ApiURL.LOGIN);
    
    private final List<String> exclusiveLogging = List.of(ApiURL.ACTUATOR_HEALTH, ApiURL.ACTUATOR_INFO);
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        RequestWrapper httpServletRequestWrapper = new RequestWrapper((HttpServletRequest) request);
        ResponseWrapper httpServletResponseWrapper = new ResponseWrapper((HttpServletResponse) response);
        chain.doFilter(httpServletRequestWrapper, httpServletResponseWrapper);
        
        boolean isExclusiveRequest = exclusiveRequestBody.stream().noneMatch(exclusiveRequestURL ->
                httpServletRequestWrapper.getRequestURI().contains(exclusiveRequestURL));
        
        boolean isExclusiveLogging = exclusiveLogging.stream().noneMatch(exclusiveRequestURL ->
                httpServletRequestWrapper.getRequestURI().contains(exclusiveRequestURL));
        
        if (isExclusiveLogging) {
            logRequestInfo(httpServletRequestWrapper, isExclusiveRequest);
            logResponseInfo(httpServletResponseWrapper, true,
                    System.currentTimeMillis() - startTime);
        }
        byte[] responseWrapper = httpServletResponseWrapper.toByteArray();
        response.getOutputStream().write(responseWrapper);
    }
    
    private void logRequestInfo(final HttpServletRequest httpServletRequest, final boolean showRequestBody)  {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n-----------REQUEST BEGIN--------------\n");
        stringBuilder.append(String.format("\t%-20s: %s%n", "IP", httpServletRequest.getRemoteAddr()));
        
        if (CommonUtil.isNonNullOrNonEmpty(httpServletRequest.getSession(false)))
            stringBuilder.append(String.format("\t%-20s: %s%n", "SessionID", httpServletRequest.getSession().getId()));
        
        if (CommonUtil.isNonNullOrNonEmpty(httpServletRequest.getMethod()))
            stringBuilder.append(String.format("\t%-20s: %s%n", "Method", httpServletRequest.getMethod()));
        
        if (CommonUtil.isNonNullOrNonEmpty(httpServletRequest.getRequestURI()))
            stringBuilder.append(String.format("\t%-20s: %s%n", "URI", httpServletRequest.getRequestURI()));
        
        if (CommonUtil.isNonNullOrNonEmpty(httpServletRequest.getQueryString()))
            stringBuilder.append(String.format("\t%-20s: %s%n", "Query", httpServletRequest.getQueryString()));
        
        String headerString = getHeadersAsStringFromRequest(httpServletRequest);
        
        if (CommonUtil.isNonNullOrNonEmpty(headerString))
            stringBuilder.append(String.format("\t%-20s: %s%n", "Headers", headerString));
        
        if (showRequestBody)
            stringBuilder.append(String.format("\t%-20s: %s%n", "Body", getRequestBody(httpServletRequest)));
        
        stringBuilder.append("\n-----------REQUEST END--------------\n");
        
        log.info(stringBuilder.toString());
    }
    
    private void logResponseInfo(final HttpServletResponse httpServletResponse, final boolean showRequestBody,
                                 long processTimeInMilliseconds) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n-----------RESPONSE BEGIN--------------\n");
        stringBuilder.append(String.format("\t%-20s: %s%n", "Status", httpServletResponse.getStatus()));
        stringBuilder.append(String.format("\t%-20s: %s (ms)%n", "Process time", processTimeInMilliseconds));
        
        String headerString = getHeadersAsStringFromResponse(httpServletResponse);
        
        if (CommonUtil.isNonNullOrNonEmpty(headerString))
            stringBuilder.append(String.format("\t%-20s: %s%n", "Headers", headerString));
        
        if (showRequestBody)
            stringBuilder.append(String.format("\t%-20s: %s%n", "Body", getResponseBody(httpServletResponse)));
        
        stringBuilder.append("\n-----------RESPONSE END--------------\n");
        
        log.info(stringBuilder.toString());
    }
    
    private String getRequestBody(HttpServletRequest httpServletRequest) {
        String body = null;
        RequestWrapper requestWrapper = (RequestWrapper) httpServletRequest;
        try {
            String charEncoding = CommonUtil.isNonNullOrNonEmpty(httpServletRequest.getCharacterEncoding()) ?
                    httpServletRequest.getCharacterEncoding()
                    : "UTF-8";
//            InputStream inputStream = httpServletRequest.getInputStream();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//            // Create a temporary storage area with the size 1024 bytes
//            // Used to storage data from inputStream
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            /*
//               Read data from inputStream
//              -1 means the end of the stream
//             */
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                // Write the data from the buffer to the output stream, starting from the index 0 up to bytesRead bytes
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
            
            body = new String(requestWrapper.toByteArray(), charEncoding);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
        return body;
    }
    
    private String getResponseBody(HttpServletResponse httpServletResponse) {
        String body = null;
        ResponseWrapper responseWrapper = (ResponseWrapper) httpServletResponse;
        try {
            String charEncoding = httpServletResponse.getCharacterEncoding();
            
            body = new String(responseWrapper.toByteArray(), charEncoding);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
        return body;
    }
    
    private String getHeadersAsStringFromRequest(HttpServletRequest request) {
        StringBuilder headerAsString = new StringBuilder();
        Enumeration<String> headers = request.getHeaderNames();
        if (CommonUtil.isNonNullOrNonEmpty(headers)) {
            headerAsString.append("[");
            while (headers.hasMoreElements()) {
                String headerName = headers.nextElement();
                headerAsString.append(headerName).append(": ");
                headerAsString.append(request.getHeaders(headerName));
                if (headers.hasMoreElements())
                    headerAsString.append(", ");
            }
            headerAsString.append("]");
        }
        return headerAsString.toString();
    }
    
    private String getHeadersAsStringFromResponse(HttpServletResponse response) {
        StringBuilder headerAsString = new StringBuilder();
        Iterator<String> headers = response.getHeaderNames().iterator();
        
        if (CommonUtil.isNonNullOrNonEmpty(headers)) {
            headerAsString.append("[");
            while (headers.hasNext()) {
                String headerName = headers.next();
                headerAsString.append(headerName).append(": ");
                headerAsString.append(response.getHeaders(headerName));
                if (headers.hasNext()) {
                    headerAsString.append(", ");
                }
            }
            headerAsString.append("]");
        }
        return headerAsString.toString();
    }
}
