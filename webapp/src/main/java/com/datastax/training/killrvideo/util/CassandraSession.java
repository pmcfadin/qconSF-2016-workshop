package com.datastax.training.killrvideo.util;

import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

// All of the DAO classes are subclasses of this
public class CassandraSession {

    public static UUID MIN_TIMEUUID_VALUE = UUIDs.startOf(-11676067200000L); // 01-01-1600
    public static UUID MAX_TIMEUUID_VALUE = UUIDs.endOf(13569494400000L); // 01-01-2100

    private static Session session = null;
    private static MappingManager mappingManager;

    /**
     * Initializes the static Cassandra session variable.
     */
    public static void initCassandra(
            String keyspace, String username,
            String password, String... contactpoints) {

        Cluster cluster = Cluster.builder()
                .withCredentials(username, password)
                .addContactPoints(contactpoints)
                .build();
        session = cluster.connect(keyspace);

        mappingManager = new MappingManager(session);
    }

    public static Session getSession() {
        return session;
    }

    public static <T> Mapper<T> getMappingManager(Class<T> clazz) {
        return mappingManager.mapper(clazz);
    }
}
