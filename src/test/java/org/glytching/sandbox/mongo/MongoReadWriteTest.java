package org.glytching.sandbox.mongo;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@Category(MongoTests.class)
public class MongoReadWriteTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoClientTest.class);

    @Test
    public void canReadDate() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("device");

        Document document = collection.find().iterator().next();
        System.out.println(document.toJson());
        // now you have a Date object ...
        Date cob = document.getDate("COBDate");

        /// ... which you can use _as is_ or format into a String as follows ...
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        /// this will print: 2018-02-15T18:30:00.000Z
        System.out.println(df.format(cob));
    }

    @Test
    public void canFindByObjectId() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("sample");

        Document document = collection.find(eq("_id", new ObjectId("59b86ff639f9ba0f9c0dccf6"))).first();

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

        Bson filter = eq("name", "beep");
        FindIterable<Document> documents = collection.find(filter).batchSize(50).limit(1);


        for (Document d : documents) {
            logger.info(d.toJson());
            logger.info(document.get("_id", ObjectId.class).toHexString());
        }

        Document updatedDocument = collection.findOneAndUpdate(filter,
                new Document("$push",
                        new BasicDBObject("terms", new BsonString("termB"))
                                .append("definitions", new BsonString("definitionB"))),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
        logger.info("Updated document: {}", updatedDocument.toJson());
    }

    @Test
    public void canSample() throws IOException {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("sample");

        AggregateIterable<Document> documents = collection.aggregate(Arrays.asList(Aggregates.sample(1)));

        for (Document d : documents) {
            logger.info(d.toJson());
        }
    }

    @Test
    public void canBulkWriteAndIdentifySpecificFailedDocuments() throws IOException {
        MongoClient mongoClient = new MongoClientFactory().create();


        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("bulkwrite");

        collection.drop();

        Document knownDocument = new Document().append("name", new BsonString("beep"));
        collection.insertOne(knownDocument);


        collection.createIndex(new BasicDBObject("name", 1), new IndexOptions().unique(true));

        int createDuplicateOnIndex = 2;
        List<Document> docs = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            if (i == createDuplicateOnIndex) {
                // deliberately trigger a duplicate key exception
                docs.add(knownDocument);
            } else {
                docs.add(new Document().append("name", new BsonString("beep" + i)));
            }

        }

        try {
            collection.insertMany(docs, new InsertManyOptions().ordered(false));
        } catch (MongoBulkWriteException ex) {
            assertThat(ex.getWriteResult().getInsertedCount(), is(4));
            assertThat(ex.getMessage(), containsString("duplicate key error"));
            assertThat(ex.getWriteErrors().size(), is(1));
            assertThat(ex.getWriteErrors().get(0).getIndex(), is(createDuplicateOnIndex));
            assertThat(ex.getWriteErrors().get(0).getCode(), is(11000));
            assertThat(ex.getWriteErrors().get(0).getMessage(), startsWith("E11000 duplicate key error"));
        }
    }

    @Test
    public void canPersistDocumentWithEmbeddedSubDocument() {
        MongoClient mongoClient = new MongoClientFactory().create();

        MongoCollection<Document> collection = mongoClient.getDatabase("stackoverflow").getCollection("subdocument");

        Document metadata = new Document("_class", "eu.ohim.rcd.efi.DocumentEntity")
                .append("_id", "RCD201800000001024-0002-0000")
                .append("provisionalId", "RCD201800000001024-0002")
                .append("designApplicationId", "RCD201800000001024")
                .append("documentType", "PROTECTED_VIEW")
                .append("rcdProtected", true);

        Document document = new Document("filename", "View000.jpg")
                .append("aliases", null)
                .append("chunkSize", 261120L)
                .append("uploadDate", new Date())
                .append("length", 29179L)
                .append("contentType", "image/jpeg")
                .append("md5", "1402721cc080174ac6ae9c1d51eb68ab")
                .append("metadata", metadata);

        collection.insertOne(document);

        Document insertedDocument = collection.find(Filters.eq("filename", "View000.jpg")).first();

        assertThat(insertedDocument.containsKey("filename"), is(true));
        assertThat(insertedDocument.getString("filename"), is(document.getString("filename")));
        assertThat(insertedDocument.getLong("chunkSize"), is(document.getLong("chunkSize")));

        assertThat(insertedDocument.containsKey("metadata"), is(true));
        Document subDocument = (Document) insertedDocument.get("metadata");
        assertThat(subDocument.getString("_id"), is(metadata.get("_id")));
        assertThat(subDocument.getString("_class"), is(metadata.get("_class")));
        assertThat(subDocument.getString("provisionalId"), is(metadata.get("provisionalId")));
        assertThat(subDocument.getString("designApplicationId"), is(metadata.get("designApplicationId")));
        assertThat(subDocument.getString("documentType"), is(metadata.get("documentType")));
        assertThat(subDocument.getBoolean("rcdProtected"), is(metadata.get("rcdProtected")));
    }
}