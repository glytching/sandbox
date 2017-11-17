package org.glytching.sandbox.hamcrest.matchers;

import org.junit.Test;

import static org.glytching.sandbox.hamcrest.matchers.IgnoresAllWhitespacesMatcher.ignoresAllWhitespaces;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class EqualsIgnoringWhitespaceTest {

    @Test
    public void testUsingHamcrestsMatcher() {
        // leading and trailing spaces are ignored
        assertThat("m 2", equalToIgnoringWhiteSpace(" m 2 "));

        // all other spaces are collapsed to a single space
        assertThat("m 2", equalToIgnoringWhiteSpace("m     2"));

        // does not match because the single space in the expected string is not collapsed any further
        assertThat("m2", not(equalToIgnoringWhiteSpace("m 2")));
    }

    @Test
    public void testUsingCustomMatcher() {
        // leading and trailing spaces are ignored
        assertThat("m2", ignoresAllWhitespaces(" m 2 "));

        // intermediate spaces are ignored
        assertThat("m2", ignoresAllWhitespaces("m     2"));
    }
}