package org.glytching.sandbox.powermock.basic;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PowermockSUT.class})
public class PowermockTest {

    @Test
    public void testMockingStatic() {
        PowerMockito.mockStatic(PowermockSUT.class);

        String expected = "expected";
        Mockito.when(PowermockSUT.grab()).thenReturn(expected);

        Assert.assertEquals(expected, PowermockSUT.grab());
    }
}
