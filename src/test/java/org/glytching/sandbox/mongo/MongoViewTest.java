package org.glytching.sandbox.mongo;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoViewTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientTest.class);

    @Test
    public void canCreateAndReadFromAView() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoDatabase database = mongoClient.getDatabase("stackoverflow");

        String viewName = "latest";

        boolean viewAlreadyExists = collectionOrViewExists(database, viewName);

        if (!viewAlreadyExists) {
            logger.info("Creating view with name={}", viewName);
            List<? extends Bson> pipeline = Lists.newArrayList(Aggregates.match(Filters.eq("name", "MongoDB")));
            database.createView("latest", "sample", pipeline);
        } else {
            logger.info("Collection/view with name={} already exists", viewName);
        }

        MongoCollection<Document> view = database.getCollection("latest");

        AggregateIterable<Document> documents = view.aggregate(Arrays.asList(Aggregates.sample(1)));

        logger.info(documents.first().toJson());

        if (!viewAlreadyExists) {
            logger.info("Dropping view with name={}", viewName);
            view.drop();
        }
    }

    @Test
    public void canCreateAndReadFromAnUnfilteredView() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoDatabase database = mongoClient.getDatabase("stackoverflow");

        String viewName = "latest";

        boolean viewAlreadyExists = collectionOrViewExists(database, viewName);

        if (!viewAlreadyExists) {
            logger.info("Creating view with name={}", viewName);
            database.createView("latest", "sample", new ArrayList<>());
        } else {
            logger.info("Collection/view with name={} already exists", viewName);
        }

        MongoCollection<Document> view = database.getCollection("latest");

        AggregateIterable<Document> documents = view.aggregate(Arrays.asList(Aggregates.sample(1)));

        logger.info(documents.first().toJson());

        if (!viewAlreadyExists) {
            logger.info("Dropping view with name={}", viewName);
            view.drop();
        }
    }

    private boolean collectionOrViewExists(MongoDatabase database, String viewName) {
        return database.listCollectionNames().into(new ArrayList<>()).contains(viewName);
    }
}