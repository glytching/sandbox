package org.glytching.sandbox.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HashCodeConverterTest {

    /**
     * Test of convert method, of class HashCodeConverter.
     */
    @Test
    public void testConvert() {
        ILoggingEvent le = mock(ILoggingEvent.class);
        StackTraceElement[] callerData = {new StackTraceElement("java.lang.String", //declaringClass
                "toString", //methodName
                "fileName", //fileName
                0)};
        when(le.getCallerData()).thenReturn(callerData);
        HashCodeConverter instance = new HashCodeConverter();
        String result = instance.convert(le);
        int iterationCount = 20;
        for(int i=0; i<iterationCount; i++) {
            String nextResult = instance.convert(le);
            assertEquals(result, nextResult);
        }
    }
}
