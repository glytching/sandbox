package org.glytching.sandbox.junit.rule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class RuleTest {
    @ClassRule
    public static final SystemPropertyRule systemPropertyRule = new SystemPropertyRule("foo", "bar");

    @Test
    public void testPropertyIsSet() {
        Assert.assertEquals("bar", System.getProperty("foo"));
    }
}
