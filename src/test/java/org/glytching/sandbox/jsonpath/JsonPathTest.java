package org.glytching.sandbox.jsonpath;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import net.minidev.json.JSONArray;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class JsonPathTest {
    private static final Logger logger = LoggerFactory.getLogger(JsonPathTest.class);
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
    private static final String MORE_JSON = "{\n" +
            "    \"tool\": \n" +
            "    {\n" +
            "        \"jsonpath\": \n" +
            "        {\n" +
            "            \"creator\": \n" +
            "            {\n" +
            "                \"name\": \"Jayway Inc.\",\n" +
            "                \"location\": \n" +
            "                [\n" +
            "                    \"Malmo\",\n" +
            "                    \"San Francisco\",\n" +
            "                    \"Helsingborg\"\n" +
            "                ]\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "\n" +
            "    \"book\": \n" +
            "    [\n" +
            "        {\n" +
            "            \"title\": \"Beginning JSON\",\n" +
            "            \"price\": 49.99\n" +
            "        },\n" +
            "\n" +
            "        {\n" +
            "            \"title\": \"JSON at Work\",\n" +
            "            \"price\": 29.99\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Test
    public void canParseToJson() {
        String json = "{\n" +
                "    \"file01\": {\n" +
                "        \"id\": \"0001\"\n" +
                "    },\n" +
                "    \"file02\": {\n" +
                "        \"id\": \"0002\"\n" +
                "    }\n" +
                "}";

        Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).build();

        JsonObject file01 = JsonPath.using(conf).parse(json).read("$.file01");

        assertThat(file01.toString(), is("{\"id\":\"0001\"}"));
    }

    @Test
    public void canReadAnIntegerAsLong() {
        String payload = "{\"order\": { \"date\": 1531380888 } }";

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

        Configuration conf = Configuration.builder()
                .jsonProvider(new JacksonJsonProvider(objectMapper))
                .build();

        Object rawJson = conf.jsonProvider().parse(payload);
        Long orderDate = JsonPath.read(rawJson, "$.order.date");

        assertThat(orderDate, is(1531380888L));
    }

    @Test
    public void canReadValueAndPathFromTheSameCompiledJsonPath() {
        String json = "{\n" +
                "      \"name\" : \"john\",\n" +
                "      \"gender\" : \"male\"\n" +
                "   }";

        Object parse = Configuration.defaultConfiguration()
                .jsonProvider()
                .parse(json);

        JsonPath path = JsonPath.compile("$.name");

        JSONArray asPath = path.read(parse, Configuration.builder()
                .options(Option.AS_PATH_LIST).build());
        assertThat(asPath.size(), is(1));
        assertThat(asPath.iterator().next(), is("$['name']"));

        String asValue = path.read(parse);
        assertThat(asValue, is("john"));
    }

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

    @Test
    public void canGetRootNodeNames() {
        Map<String, Object> read = JsonPath.read(MORE_JSON, "$");

        assertThat(read.size(), is(2));

        assertThat(read.keySet(), hasItem("tool"));
        assertThat(read.keySet(), hasItem("book"));

        // or if you want a String[] then ...
        String[] rootNodeNames = read.keySet().toArray(new String[read.size()]);
        assertThat(rootNodeNames, Matchers.both(arrayWithSize(2)).and(arrayContainingInAnyOrder("book", "tool")));

        // or hide it all behind the hasJsonPath matcher
        assertThat(MORE_JSON, hasJsonPath("$", Matchers.hasKey("tool")));
        assertThat(MORE_JSON, hasJsonPath("$", Matchers.hasKey("book")));
    }
}
