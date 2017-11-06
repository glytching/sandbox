package org.glytching.sandbox;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CalculatorTest {

    private Calculator sut = new Calculator();

    @Test
    public void canAdd() {
        assertThat(sut.add(1, 1), is(2));
    }
}
