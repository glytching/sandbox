package org.glytching.sandbox.junit.junit5;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AssertionMethodsTest {

    @SuppressWarnings("unused")
    private static Stream<Arguments> assertParametrically() {
        String sut = "sut";
        return Stream.of(
                Arguments.of("canConstruct", (Executable) () -> assertNotNull(sut)),
                Arguments.of("notEquals", (Executable) () -> assertNotEquals("not sut", sut)));
    }

    @Test
    public void assertOneByOne() {
        String sut = "sut";
        assertNotNull(sut, "canConstruct");
        assertNotEquals("not sut", sut, "notEquals");
    }

    @Test
    public void assertTogether() {
        String sut = "sut";
        assertAll(
                () -> assertNotNull(sut, "canConstruct"),
                () -> assertNotEquals("not sut", sut, "notEquals")
        );
    }

    @TestFactory
    public Stream<DynamicTest> assertDynamically() {
        String sut = "sut";
        return Stream.of(
                dynamicTest("canConstruct", () -> assertNotNull(sut)),
                dynamicTest("notEquals", () -> assertNotEquals("not sut", sut)));
    }

    @ParameterizedTest
    @MethodSource
    public void assertParametrically(String caption, Executable executable) {
        assertDoesNotThrow(executable, caption);
    }
}
