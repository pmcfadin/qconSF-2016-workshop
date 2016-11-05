package com.datastax.training.killrvideo.util;

import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created on 13/10/2015.
 */
@Provider
public class SearchSession {

    public final Config config = ConfigFactory.load();
    private static final Logger log = LoggerFactory.getLogger(SearchSession.class);

    private static Session session;
    private static MappingManager mappingManager;

    public static Session getSession() {
        return session;
    }

    public static MappingManager getMappingManager() {
        return mappingManager;
    }

    public SearchSession() {
        // empty default constructor?
    }

    /**
     * @param username
     * @param password
     * @param dcname
     * @param contactPoints
     */
    public static void initSearchSession(String username, String password, String dcname, String keyspace,
            String... contactPoints) {

        session = Cluster.builder().addContactPoints(contactPoints).withCredentials(username, password)
                // .withLoadBalancingPolicy(new TokenAwarePolicy(
                // DCAwareRoundRobinPolicy.builder()
                // .withLocalDc(dcname)
                // .build()))
                .build().connect(keyspace);

        mappingManager = new MappingManager(session);

        log.info("Created search session; Connected to: " + session.getCluster().getMetadata().getClusterName());

    }

}
