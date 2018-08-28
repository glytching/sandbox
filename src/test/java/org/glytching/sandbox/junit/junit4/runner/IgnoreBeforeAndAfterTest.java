package org.glytching.sandbox.junit.junit4.runner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(IgnoreBeforeAndAfterRunner.class)
public class IgnoreBeforeAndAfterTest {

    @Before
    public void setUp() {
        Assert.fail("The @Before method should be ignored!");
    }

    @After
    public void tearDown() {
        Assert.fail("The @After method should be ignored!");
    }

    @Test
    public void canIgnoreBeforeAndAfter() {
        assertTrue(true);
    }
}
