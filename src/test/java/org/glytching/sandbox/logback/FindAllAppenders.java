package org.glytching.sandbox.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FindAllAppenders {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FindAllAppenders.class);

    @Test
    public void findAllAppenders() {
        logger.info("here");
        Map<String, Appender<ILoggingEvent>> appendersMap = getAppendersMap();

        for (Map.Entry<String, Appender<ILoggingEvent>> entry : appendersMap.entrySet()) {
            logger.info("key={}, value={}", entry.getKey(), entry.getValue().getContext());
        }
    }

    private Map<String, Appender<ILoggingEvent>> getAppendersMap() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        Map<String, Appender<ILoggingEvent>> appendersMap = new HashMap<>();
        for (Logger logger : loggerContext.getLoggerList()) {

            Iterator<Appender<ILoggingEvent>> appenderIterator = logger.iteratorForAppenders();
            while (appenderIterator.hasNext()) {
                Appender<ILoggingEvent> appender = appenderIterator.next();
                if (!appendersMap.containsKey(appender.getName())) {
                    appendersMap.put(appender.getName(), appender);
                }
            }
        }

        return appendersMap;
    }
}