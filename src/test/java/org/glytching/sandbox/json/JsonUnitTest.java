package org.glytching.sandbox.json;

import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.containsString;

public class JsonUnitTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canAssertJsonEquality() {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void canAssertJsonEqualityRegardlessOfPropertyOrder() {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop2\": \"value1\",\n" +
                "  \"prop1\": \"value1\"\n" +
                "}";

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void willFailIfActualIsMissingAProperty() {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("Different keys found in node \"\", expected: <[prop1, prop2]> but was: <[prop2]>. Missing: \"prop1\" "));

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void willFailIfActualHasAnIncorrectValue() {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value2\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("Different value found in node \"prop1\", expected: <\"value1\"> but was: <\"value2\">."));

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    public void canIgnoreFieldsWhichAreInActualButNotInExpected() {
        String expected = "{\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        JsonAssert.assertJsonEquals(expected, actual, JsonAssert.when(Option.IGNORING_EXTRA_FIELDS));
    }
}
