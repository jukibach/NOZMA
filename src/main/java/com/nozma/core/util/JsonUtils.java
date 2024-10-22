package com.nozma.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
    
    private JsonUtils() {
    }
    
    private static final ObjectWriter objectWriter;
    
    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }
    
    public static String convertToJson(Object object) {
        try {
            return objectWriter.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}
