package org.glytching.sandbox.json;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.Matchers.containsString;

public class JsonAssertTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canAssertJsonEquality() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void canAssertJsonEqualityRegardlessOfPropertyOrder() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop2\": \"value1\",\n" +
                "  \"prop1\": \"value1\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void willFailIfActualIsMissingAProperty() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("\n" +
                "Expected: prop1\n" +
                "     but none found\n"));

        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void willFailIfActualHasAnIncorrectValue() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value2\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("prop1\n" +
                "Expected: value1\n" +
                "     got: value2\n"));

        JSONAssert.assertEquals(expected, actual, true);
    }
}