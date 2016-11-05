package com.datastax.training.killrvideo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * DataStax Academy Sample Application
 * <p>
 * Copyright 2015 DataStax
 */

public class CassandraSessionTest {

    @Test
    public void testSession() {

        // Load in the configuration parameters from the test resources

        Config config = ConfigFactory.load("test-properties");

        String keyspace = config.getString(ConfigItems.CASSANDRA_KEYSPACE);
        String username = config.getString(ConfigItems.CASSANDRA_USERNAME);
        String password = config.getString(ConfigItems.CASSANDRA_PASSWORD);
        String[] contactpoints = config.getString(ConfigItems.CASSANDRA_HOSTS).split(",");

        // Create test keyspace
        Cluster.builder().addContactPoints(contactpoints).build().connect().execute("CREATE KEYSPACE IF NOT EXISTS "
                + keyspace + " WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor':1}");

        // Set up a CassandraSession

        CassandraSession.initCassandra(keyspace, username, password, contactpoints);

        // Validate that it works

        Session session = CassandraSession.getSession();
        assertNotNull(session);
        assertEquals(keyspace, session.getLoggedKeyspace());

    }

}
