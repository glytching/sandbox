package org.glytching.sandbox.powermock.finals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FinalClassSUT.class})
public class PowermockFinalClassTest {

    @Test
    public void testMockingStatic() {
        FinalClassSUT view = PowerMockito.mock(FinalClassSUT.class);

        String expected = "mocked call";
        Mockito.when(view.getSomething()).thenReturn(expected);

        Assert.assertEquals(expected, view.getSomething());
    }
}
