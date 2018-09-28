package org.glytching.sandbox.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;

// see https://stackoverflow.com/questions/52547139/mocking-deleteresult-of-mongodb-using-mockito-in-java
@RunWith(MockitoJUnitRunner.class)
public class MongoMockitoTest {

    private String databaseName = "aDatabase";

    @Mock
    private MongoClient mongoClient;
    @Mock
    private MongoCollection<Document> mockCollection;
    @Mock
    private MongoDatabase mockDatabase;
    @Mock
    private LinkedList<String> mockList;
    @Mock
    private MongoIterable<String> mockIterable;
    @Mock
    private DeleteResult mockDeleteResult;

    @Test
    public void testDelete() {
        String xValue = "xValue";
        String zValue = "zValue";
        String yValue = "yValue";
        long deleteCount = 1L;

        Mockito.when(mongoClient.getDatabase(Mockito.anyString())).thenReturn(mockDatabase);
        MongoIterable<String> mongoIter = Mockito.mock(MongoIterable.class);
        Mockito.when(mockDatabase.listCollectionNames()).thenReturn(mongoIter);
        Mockito.when(mongoIter.into(new LinkedList<>())).thenReturn(mockList);
        Mockito.when(mockList.contains(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockDatabase.getCollection(Mockito.anyString())).thenReturn(mockCollection);
        Mockito.when(mockCollection.deleteOne(
                argThat(new BsonMatcher(Filters.and(
                        Filters.eq("x", xValue),
                        Filters.eq("y", yValue),
                        Filters.eq("z", zValue)))))
        ).thenReturn(mockDeleteResult);


        Mockito.when(mockDeleteResult.getDeletedCount()).thenReturn(deleteCount);

        assertThat(deleteDocument(xValue, yValue, zValue), equalTo(deleteCount));
    }

    private long deleteDocument(String xValue, String yValue, String zValue) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);

        boolean collectionExists = database.listCollectionNames().into(new LinkedList<>())
                .contains(yValue);

        if (collectionExists) {
            MongoCollection<Document> collectionDocs = database.getCollection(yValue);

            DeleteResult deleteResult =
                    collectionDocs.deleteOne(Filters.and(Filters.eq("x", xValue), Filters.eq("y", yValue),
                            Filters.eq("z", zValue)));

            return deleteResult.getDeletedCount();
        }

        return 0;
    }

    // a custom matcher for Bson because it does not implement equals()
    public class BsonMatcher implements ArgumentMatcher<Bson> {

        private BsonDocument left;

        public BsonMatcher(Bson left) {
            this.left = left.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
        }

        @Override
        public boolean matches(Bson right) {
            // compare as BsonDocument, since this does provide an equals()
            return left.equals(right.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry()));
        }
    }
}
