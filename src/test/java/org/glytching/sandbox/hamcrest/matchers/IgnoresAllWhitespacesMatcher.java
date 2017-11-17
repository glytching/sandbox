package org.glytching.sandbox.hamcrest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class IgnoresAllWhitespacesMatcher extends BaseMatcher<String> {
    public String expected;

    public static IgnoresAllWhitespacesMatcher ignoresAllWhitespaces(String expected) {
        return new IgnoresAllWhitespacesMatcher(expected);
    }

    private IgnoresAllWhitespacesMatcher(String expected) {
        this.expected = expected.replaceAll("\\s+", "");
    }

    @Override
    public boolean matches(Object actual) {
        return expected.equals(actual);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("the given String should match '%s' without whitespaces", expected));
    }
}
