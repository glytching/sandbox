package org.glytching.sandbox.junit.junit5.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ExceptionTest {

    private ExceptionSUT sut;

    @BeforeEach
    public void prepare() {
        // cannot use the MockitoExtension because it requires Mockito >=2.17.0 and Powermock is not (yet) compatible
        // with any Mockito version > 2.9.0
        sut = mock(ExceptionSUT.class);
    }

    @Test
    public void aTest() {
        IllegalArgumentException expected = new IllegalArgumentException("aMessage");
        Mockito.doThrow(expected).when(sut).doIt();

        RuntimeException actual = assertThrows(IllegalArgumentException.class, () -> {
            sut.doIt();
        });
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}