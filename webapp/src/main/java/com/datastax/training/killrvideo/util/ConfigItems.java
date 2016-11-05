package com.datastax.training.killrvideo.util;

/**
 * Created on 17/10/2015.
 */
public interface ConfigItems {
    String CASSANDRA_HOSTS = "cassandra.hosts";
    String CASSANDRA_DC_NAME = "cassandra.dc.name";
    String CASSANDRA_USERNAME = "cassandra.username";
    String CASSANDRA_PASSWORD = "cassandra.password";
    String CASSANDRA_KEYSPACE = "cassandra.keyspace";

    String APP_LISTEN_ADDRESS = "app.listenaddress";

    String SEARCH_HOSTS = "search.hosts";
    String SEARCH_DC_NAME = "search.dc.name";
    String SEARCH_LISTEN_ADDRESS = "search.listenaddress";
}
