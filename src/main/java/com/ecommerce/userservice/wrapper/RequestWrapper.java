package com.ecommerce.userservice.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.input.TeeInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RequestWrapper extends HttpServletRequestWrapper {
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final CaptureResponseInputStream captureResponseInputStream = new CaptureResponseInputStream(byteArrayOutputStream);
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
    }
    
    public byte[] toByteArray() {
        return byteArrayOutputStream.toByteArray();
    }
    @Override
    public ServletInputStream getInputStream() {
        return captureResponseInputStream;
    }
    
    public class CaptureResponseInputStream extends ServletInputStream {
        private final TeeInputStream teeInputStream;
        
        public CaptureResponseInputStream(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
            this.teeInputStream = new TeeInputStream(RequestWrapper.super.getInputStream(), byteArrayOutputStream);;
        }
        
        @Override
        public boolean isFinished() {
            return false;
        }
        
        @Override
        public boolean isReady() {
            return true;
        }
        
        @Override
        public void setReadListener(ReadListener listener) {
            // this method is empty
        }
        
        @Override
        public int read() throws IOException {
            return teeInputStream.read();
        }
    }
}
