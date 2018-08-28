package org.glytching.sandbox.json;

import io.github.glytching.junit.extension.exception.ExpectedException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class JsonAssertTest {

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
    @ExpectedException(type = AssertionError.class, messageContains = "\nExpected: prop1\n     but none found\n")
    public void willFailIfActualIsMissingAProperty() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    @ExpectedException(type = AssertionError.class, messageContains = "prop1\nExpected: value1\n     got: value2\n")
    public void willFailIfActualHasAnIncorrectValue() throws JSONException {
        String expected = "{\n" +
                "  \"prop1\": \"value1\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";
        String actual = "{\n" +
                "  \"prop1\": \"value2\",\n" +
                "  \"prop2\": \"value1\"\n" +
                "}";

        JSONAssert.assertEquals(expected, actual, true);
    }
}