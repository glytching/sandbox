package org.glytching.sandbox.logback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

public class LogbackTest {
    private final Logger logger = LoggerFactory.getLogger(LogbackTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testComplexValueInMdc() throws JsonProcessingException {
        Map<String, Object> complexMdcValue = new HashMap<>();
        Map<String, Object> childMdcValue = new HashMap<>();
        childMdcValue.put("name", "Joe");
        childMdcValue.put("type", "Martian");
        complexMdcValue.put("child", childMdcValue);
        complexMdcValue.put("category", "etc");

        MDC.put("complexNestedValue", objectMapper.writeValueAsString(complexMdcValue));
        logger.info("hello!");
    }

    @Test
    public void testJsoninLogMessage() {
        MDC.put("application", "payment");
        logger.info("{\"a\": 1, \"b\": 2}");
    }

    @Test
    public void testLogException() {
        logger.info("Boom!", new RuntimeException("ouch"));
    }

    @Test
    public void testLog() {
        logger.error("Boom!", new RuntimeException("ouch"));
    }
}