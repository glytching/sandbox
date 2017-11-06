package org.glytching.sandbox;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class AnyTest {

    @Test
    public void aTest() {
        assertThat((1+1), is(2));
    }

    @Test
    public void bTest() {
        assertThat((1+1), not(3));
    }
}
