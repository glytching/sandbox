package org.glytching.sandbox.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

public class MongoClientFactory {

    public MongoClient create() {
        MongoClientOptions clientOptions = MongoClientOptions.builder()
                .connectTimeout(1000)
                .socketTimeout(1000)
                .serverSelectionTimeout(1000)
                .build();

        return new MongoClient(new ServerAddress("localhost", 27017), clientOptions);
    }
}
