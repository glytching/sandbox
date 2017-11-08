package org.glytching.sandbox.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.ListDatabasesIterable;
import org.bson.Document;
import org.glytching.sandbox.surefire.MongoTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Category(MongoTests.class)
public class MongoClientTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientTest.class);

    @Test
    public void canConnect() {
        MongoClient mongoClient = new MongoClientFactory().create();

        ListDatabasesIterable<Document> databases = mongoClient.listDatabases();
        for (Document d : databases) {
            logger.info(d.toJson());
        }
    }
}
