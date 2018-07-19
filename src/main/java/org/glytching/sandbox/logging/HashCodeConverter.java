package org.glytching.sandbox.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class HashCodeConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent le) {

            StackTraceElement[] cda = le.getCallerData();
            if (cda != null && cda.length > 0) {
                // it's not clear to me exactly what you need from this hashcode
                // this approach will give you the same value for each logger invocation from a given type
                // but it will not give you the same result for every logger invocation from every instance of a given
                // type because it is returning the hashCode of a class not an instance
                // the actual instance from which a logger event emerges is not available in the logging context
                return Integer.toString(System.identityHashCode(cda[0].getClass()));
            } else {
                return CallerData.NA;
            }
    }
}
