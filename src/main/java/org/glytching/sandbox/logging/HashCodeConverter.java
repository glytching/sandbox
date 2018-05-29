package org.glytching.sandbox.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class HashCodeConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent le) {
            StackTraceElement[] cda = le.getCallerData();
            if (cda != null && cda.length > 0) {
                return Integer.toString(System.identityHashCode(cda[0]));
            } else {
                return CallerData.NA;
            }
    }
}
