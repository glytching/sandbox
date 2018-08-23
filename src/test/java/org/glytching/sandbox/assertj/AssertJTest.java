package org.glytching.sandbox.assertj;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertJTest {

    @Test
    public void canMatchArrayWithMultipleValuesByTypeAndContents() {
        Object[] array = new Object[2];
        String[] firstElement = new String[]{"Hello"};
        String[] secondElement = new String[]{"World"};
        array[0] = firstElement;
        array[1] = secondElement;
        assertThat(array).containsExactlyInAnyOrder(firstElement, secondElement);
    }

    @Test(expected = AssertionError.class)
    public void cannotMatchArrayWithSingleValueByNonSpecificType() {
        Object[] array = new Object[1];
        String[] element = new String[]{"Hello"};
        array[0] = element;
        assertThat(array).containsExactlyInAnyOrder(element);
    }
}
