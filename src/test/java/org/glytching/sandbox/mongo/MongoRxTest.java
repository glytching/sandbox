package org.glytching.sandbox.mongo;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import org.bson.Document;
import org.glytching.sandbox.surefire.MongoTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observer;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Projections.excludeId;
import static java.util.concurrent.TimeUnit.SECONDS;

@Category(MongoTests.class)
public class MongoRxTest {
    private static final Logger logger = LoggerFactory.getLogger(MongoRxTest.class);

    // see http://mongodb.github.io/mongo-java-driver-rx/1.4/getting-started/quick-tour/
    @Test
    public void testReadWrite() throws InterruptedException {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

        MongoDatabase database = mongoClient.getDatabase("stackoverflow");
        MongoCollection<Document> collection = database.getCollection("sample");

        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            documents.add(new Document("name", "MongoDB")
                    .append("type", "database")
                    .append("count", i));
        }

        // it's a cold observable so it won't return anything until we subscribe to it ... toBlocking is a form of subscription
        // collection.insertMany(documents).timeout(10, SECONDS).toBlocking().single();

        // or else subscribe explicitly ...
        TestSubscriber<Object> insertSubscriber = printSubscriber("Insert = ");
        collection.insertMany(documents).timeout(10, SECONDS).subscribe(insertSubscriber);
        // wait for this to complete since it is a pre condition for the subsequent count
        insertSubscriber.awaitTerminalEvent();

        TestSubscriber<Object> countSubscriber = printSubscriber("Count = ");
        collection.count().subscribe(countSubscriber);
        countSubscriber.awaitTerminalEvent();

        collection.find(gt("count", 5))
                .projection(excludeId())
                .sort(new Document("count", -1))
                .toObservable().toBlocking()
                .forEach(document -> System.out.println(document.toJson()));

        CountDownLatch latch = new CountDownLatch(1);
        List<Throwable> failures = new ArrayList<>();
        collection.find()
                .toObservable().subscribe(
                // on next, this is invoked for each document returned by your find call
                document -> {
                    // presumably you'll want to do something here to meet this requirement: "pass it on to test in JUnit5"
                    logger.info(document.toJson());
                },
                /// on error
                failures::add,
                // on completion
                latch::countDown);
        latch.await();
    }

    private <T> TestSubscriber<T> printSubscriber(final String onStartMessage) {
        return new TestSubscriber<>(new Observer<T>() {
            private boolean first = true;

            @Override
            public void onCompleted() {
                System.out.println();
            }

            @Override
            public void onError(final Throwable t) {
                System.out.println("The Observer errored: " + t.getMessage());
            }

            @Override
            public void onNext(final T t) {
                if (first && onStartMessage != null) {
                    System.out.print(onStartMessage);
                    first = false;
                }
                System.out.print(t + " ");
            }
        });
    }
}