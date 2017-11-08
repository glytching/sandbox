package org.glytching.sandbox.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class CreateLoggerTest {

    @Test
    public void canCreateALogger() {
        Logger logger = createLoggerFor("foo", "target/logs/testLogback.log");
        logger.debug("A log message written from a programmatically created appender ...");
    }

    private Logger createLoggerFor(String string, String file) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        RollingFileAppender logFileAppender = new RollingFileAppender();
        logFileAppender.setContext(lc);
        logFileAppender.setName("debug");
        logFileAppender.setEncoder(ple);
        logFileAppender.setAppend(true);
        logFileAppender.setFile(file);
        TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
        logFilePolicy.setContext(lc);
        logFilePolicy.setParent(logFileAppender);
        logFilePolicy.setFileNamePattern(file + ".%d{yyyy-MM-dd}.%i");
        logFilePolicy.setMaxHistory(7);
        logFilePolicy.start();

        logFileAppender.setRollingPolicy(logFilePolicy);
        logFileAppender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(string);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false);


        logger.addAppender(logFileAppender);

        return logger;
    }
}