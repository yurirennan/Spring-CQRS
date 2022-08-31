package com.yuri.simplescqrs.demo.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final String database;
    private final String port;
    private final String hostname;

    public MongoConfig(
            @Value("${mongodb.connection.database}") final String database,
            @Value("${mongodb.connection.port}") final String port,
            @Value("${mongodb.connection.hostname}") final String hostname
    ) {
        this.database = database;
        this.port = port;
        this.hostname = hostname;
    }

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(this.getUri());

        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    private String getUri() {
        final String pattern = "mongodb://%s:%s/%s";

        return String.format(
                pattern,
                this.hostname,
                this.port,
                this.getDatabaseName()
        );
    }
}
