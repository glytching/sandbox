package org.glytching.sandbox.junit.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionTest {

    @Mock
    private ExceptionSUT sut;

    @Test(expected = IllegalArgumentException.class)
    public void aTest() {
        IllegalArgumentException expected = new IllegalArgumentException();
        Mockito.doThrow(expected).when(sut).doIt();

        sut.doIt();
    }
}