package org.glytching.sandbox.logging;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

public class ExceptionNameConverter extends ThrowableProxyConverter {
    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return tp.getClassName();
    }
}