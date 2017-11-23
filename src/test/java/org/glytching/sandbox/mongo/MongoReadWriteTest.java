package org.glytching.sandbox.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.glytching.sandbox.surefire.MongoTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Category(MongoTests.class)
public class MongoReadWriteTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientTest.class);

    @Test
    public void canFindByObjectId() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("sample");

        Document document = collection.find(Filters.eq("_id", new ObjectId("59b86ff639f9ba0f9c0dccf6"))).first();

        logger.info(document.toJson());
    }

    @Test
    public void canWriteAndRead() throws IOException {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("sample");

        List<BsonString> terms = new ArrayList<>();
        terms.add(new BsonString("termA"));

        List<BsonString> definitions = new ArrayList<>();
        definitions.add(new BsonString("definitionA"));

        Document document = new Document()
                .append("name", new BsonString("beep"));
        document.put("terms", terms);
        document.put("definitions", definitions);

        collection.insertOne(document);

        Bson filter = Filters.eq("name", "beep");
        FindIterable<Document> documents = collection.find(filter).batchSize(50).limit(1);


        for (Document d : documents) {
            logger.info(d.toJson());
        }

        Document updatedDocument = collection.findOneAndUpdate(filter,
                new Document("$push",
                        new BasicDBObject("terms", new BsonString("termB"))
                                .append("definitions", new BsonString("definitionB"))),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
        logger.info("Updated document: {}", updatedDocument.toJson());
    }
}