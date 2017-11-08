package org.glytching.sandbox.jsonpath;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonPathTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonPathTest.class);

    @Test
    public void canReadUsingACombinedFilter() {
        Filter combinedFilter = Filter.filter(
                Criteria.where("$.store.book[?].category").is("reference").and("$.store.book[?].author").is("Nigel Rees")
        );

        Map<String, Object> book = JsonPath.parse(JSON).read("$", combinedFilter);

        logger.info("Read book: {}", book);
    }

    @Test
    public void canReadAuthors() {
        List<String> authors = JsonPath.read(JSON, "$.store.book[*].author");

        assertThat(authors.size(), is(4));
        assertThat(authors, hasItem("Nigel Rees"));
        assertThat(authors, hasItem("Evelyn Waugh"));
        assertThat(authors, hasItem("Herman Melville"));
        assertThat(authors, hasItem("J. R. R. Tolkien"));
    }


    private static final String JSON = "{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"category\": \"reference\",\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 12.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Herman Melville\",\n" +
            "                \"title\": \"Moby Dick\",\n" +
            "                \"isbn\": \"0-553-21311-3\",\n" +
            "                \"price\": 8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"J. R. R. Tolkien\",\n" +
            "                \"title\": \"The Lord of the Rings\",\n" +
            "                \"isbn\": \"0-395-19395-8\",\n" +
            "                \"price\": 22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\": {\n" +
            "            \"color\": \"red\",\n" +
            "            \"price\": 19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"expensive\": 10\n" +
            "}";
}
