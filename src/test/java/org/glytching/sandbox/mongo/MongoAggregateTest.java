package org.glytching.sandbox.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoAggregateTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientTest.class);

    @Test
    public void canEval() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoDatabase database = mongoClient.getDatabase("stackoverflow");

        Bson command = new Document("eval", "db.sample.find({'name': 'Craig'}).count();");

        Document result = database.runCommand(command);
        logger.info(result.toJson());
    }

    @Test
    public void canCompareDateFields() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("articles");

        List<Document> documents = collection.aggregate(Arrays.asList(
                new Document("$redact", new Document("$cond",
                        Arrays.asList(new Document("$gt", Arrays.asList("$pub-date", "$rel-date")), "$$KEEP", "$$PRUNE"))
                ))).into(new ArrayList<>());

        for (Document document : documents) {
            logger.info("{}", document.toJson());
        }
    }

    @Test
    public void canFilterSubDocumentArray() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("sample");

        collection.insertOne(Document.parse("{\n" +
                "    \"data1\" : \"value1234\",\n" +
                "    \"data2\" : \"value2\",\n" +
                "    \"array1\" : [ \n" +
                "        {\n" +
                "            \"field1\" : \"field1value\",\n" +
                "            \"field2\" : \"field2value\"\n" +
                "        }, \n" +
                "        {\n" +
                "            \"field1\" : \"expectedvalue\",\n" +
                "            \"field2\" : \"field2value\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"));

        List<Document> documents = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("data1", "value1234")),
                new Document("$project", new Document()
                        // include data1
                        .append("data1", 1)
                        // include data2
                        .append("data2", 1)
                        // include only those elements of array1 which match the filter condition
                        .append("array1", new Document("$filter",
                                new Document("input", "$array1")
                                        .append("as", "a")
                                        .append("cond", new Document("$eq", Arrays.asList("$$a.field1",
                                                "expectedvalue"))))))

        )).into(new ArrayList<>());

        for (Document document : documents) {
            logger.info("{}", document.toJson());
        }

        // clean up
        collection.deleteMany(Filters.eq("data1", "value1234"));
    }

    @Test
    public void canFilterSubDocumentArrayWithElemMatch() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("still");

        Document filter = new Document("senses", new BasicDBObject("$exists", true))
                .append("senses.lang", new BasicDBObject("$eq", "EN"))
                .append("senses.source", new BasicDBObject("$eq", "EN_DICTIONARY"));

        DBObject filterExpression = new BasicDBObject();
        filterExpression.put("input", "$senses");
        filterExpression.put("as", "sense");
        filterExpression.put("cond", new BasicDBObject("$and", Arrays.<Object>asList(
                new BasicDBObject("$eq", Arrays.<Object>asList("$$sense.lang", "EN")),
                new BasicDBObject("$eq", Arrays.<Object>asList("$$sense.source", "EN_DICTIONARY")))
        ));

        BasicDBObject projectionFilter = new BasicDBObject("$filter", filterExpression);

        AggregateIterable<Document> documents = collection.aggregate(Arrays.asList(
                new Document("$match", filter),
                new Document("$project", new Document("senses", projectionFilter))
        ));

        for (Document document : documents) {
            logger.info("{}", document.toJson());
        }
    }

    @Test
    @Disabled
    public void aDayOfYearAggregation() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("dayOfYear");

        Document project = new Document("name", 1)
                .append("count", 1)
                .append("dayOfYear", new Document("$dayOfYear", "$TIMESTAMP"));

        Document dayOfYearMatch = new Document("dayOfYear", 275);

        Document grouping = new Document("_id", "$name").append("total", new Document("$sum", "$count"));

        AggregateIterable<Document> documents = collection.aggregate(Arrays.asList(
                new Document("$project", project),
                new Document("$match", dayOfYearMatch),
                new Document("$group", grouping)
        ));

        for (Document document : documents) {
            logger.info("{}", document.toJson());
        }
    }

    @Test
    @Disabled
    public void aDayOfYearAggregationUsingRedact() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("dayOfYear");

        Document redact =
                new Document("$cond", new Document("if", new Document("$eq", Arrays.asList(new Document("$dayOfYear",
                        "$TIMESTAMP"), 275)))
                        .append("then", "$$KEEP")
                        .append("else", "$$PRUNE"));

        Document grouping = new Document("_id", "$name").append("total", new Document("$sum", "$count"));

        AggregateIterable<Document> documents = collection.aggregate(Arrays.asList(
                new Document("$redact", redact),
                new Document("$group", grouping)
        ));

        for (Document document : documents) {
            logger.info("{}", document.toJson());
        }
    }
}
