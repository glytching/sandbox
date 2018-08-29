package org.glytching.sandbox.junit.junit5.parameterized;

import org.glytching.sandbox.junit.ParameterisedSUT;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterisedTest {
    private static final Logger logger = LoggerFactory.getLogger(ParameterisedTest.class);
    private static final ParameterisedSUT sut = new ParameterisedSUT();

    @SuppressWarnings("unused")
    private static Stream<Arguments> assertParametrically() {
        String sut = "sut";
        return Stream.of(
                Arguments.of("isAPrimeNumber", (Executable) () -> {
                    int arg = 2;
                    logger.info("Parameterised Number is: {}", arg);
                    assertTrue(ParameterisedTest.sut.validate(arg));
                }),
                Arguments.of("isNotAPrimeNumber", (Executable) () -> {
                    int arg = 6;
                    logger.info("Parameterised Number is: {}", arg);
                    assertFalse(ParameterisedTest.sut.validate(arg));
                }),
                Arguments.of("isAPrimeNumber", (Executable) () -> {
                    int arg = 19;
                    logger.info("Parameterised Number is: {}", arg);
                    assertTrue(ParameterisedTest.sut.validate(arg));
                }),
                Arguments.of("isNotAPrimeNumber", (Executable) () -> {
                    int arg = 22;
                    logger.info("Parameterised Number is: {}", arg);
                    assertFalse(ParameterisedTest.sut.validate(arg));
                })
        );
    }

    @ParameterizedTest
    @MethodSource
    public void assertParametrically(String caption, Executable executable) {
        assertDoesNotThrow(executable, caption);
    }
}