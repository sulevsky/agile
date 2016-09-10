package org.sulevsky.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.List;

import static com.mongodb.MongoCredential.createCredential;
import static java.util.Collections.singletonList;

@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    private final String host;
    private final int port;
    private final String database;
    private final String userName;
    private final String password;

    public MongoDbConfig(@Value("${mongo.host}") String host,
                         @Value("${mongo.port}") int port,
                         @Value("${mongo.database}") String database,
                         @Value("${mongo.username}") String userName,
                         @Value("${mongo.password}") String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public Mongo mongo() {
        return new MongoClient(new ServerAddress(host, port), mongoCredentials());
    }

    private List<MongoCredential> mongoCredentials() {
        return singletonList(createCredential(
                userName,
                database,
                password.toCharArray()));
    }

}
