package org.glytching.sandbox.logging;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;

public class ExceptionNameConverter extends ThrowableProxyConverter {
    @Override
    protected String throwableProxyToString(IThrowableProxy tp) {
        return tp.getClassName();
    }
}

/*
I think the closest you can get to this out of the box is to use the [`%ex` conversion keyword](https://logback.qos.ch/manual/layouts.html#ex) with a depth of `1`.

For example, using this pattern:

    %d{yyyy-MM-dd}|%-5level|%exception{1}|%m%n

The following log statement:

    logger.error("Boom!", new RuntimeException("ouch"));

Will emit:

    2018-09-26|ERROR|java.lang.RuntimeException: ouch
        at org.glytching.sandbox.logback.LogbackTest.testLog(LogbackTest.java:43)
    |Boom!

So, in order to produce what you want I think you'll need to write your own [custom conversion specifier](https://logback.qos.ch/manual/layouts.html#customConversionSpecifier).

You can declare a conversion rule in your `logback.xml` for the `%exname` symbolic like so:

    <?xml version="1.0" encoding="UTF-8" ?>
    <configuration>
        <conversionRule conversionWord="ex" converterClass="com.foo.ExceptionNameConverter" />

        ...

    </configuration>

Then declare `ExceptionNameConverter` like so:


    import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
    import ch.qos.logback.classic.spi.IThrowableProxy;

    public class ExceptionNameConverter extends ThrowableProxyConverter {
        @Override
        protected String throwableProxyToString(IThrowableProxy tp) {
            return tp.getClassName();
        }
    }

Now, using this pattern:

    %d{yyyy-MM-dd}|%-5level|%exname|%m%n

The following log statement:

    logger.error("Boom!", new RuntimeException("ouch"));

Will emit:

    2018-09-26|ERROR|java.lang.RuntimeException|Boom!


*/