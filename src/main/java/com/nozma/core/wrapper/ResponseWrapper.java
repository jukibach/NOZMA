package com.nozma.core.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private final CaptureResponseOutputStream captureResponseOutputStream = new CaptureResponseOutputStream(bos);
    private final PrintWriter printWriter = new PrintWriter(captureResponseOutputStream);
    
    /**
     * Constructs a request object wrapping the given response.
     *
     * @param response The request to wrap
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }
    
    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }
    
    @Override
    public ServletOutputStream getOutputStream() {
        return captureResponseOutputStream;
    }
    
    public byte[] toByteArray() {
        printWriter.flush();
        return bos.toByteArray();
    }
    
    @Override
    public ServletResponse getResponse() {
        return this;
    }
    
    public static class CaptureResponseOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream byteArrayOutputStream;
        
        public CaptureResponseOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
            this.byteArrayOutputStream = byteArrayOutputStream;
        }
        
        @Override
        public boolean isReady() {
            return true;
        }
        
        @Override
        public void setWriteListener(WriteListener listener) {
            // this method is empty
        }
        
        @Override
        public void write(int b) {
            byteArrayOutputStream.write(b);
        }
    }
}
