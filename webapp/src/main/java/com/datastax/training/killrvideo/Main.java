package com.datastax.training.killrvideo;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.datastax.training.killrvideo.model.dao.UserDAO;
import com.datastax.training.killrvideo.util.CassandraSession;
import com.datastax.training.killrvideo.util.ConfigItems;
import com.datastax.training.killrvideo.util.SearchSession;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.datastax.training.killrvideo.model.dao.UserSessionDAO;
import com.datastax.training.killrvideo.model.dao.VideoDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserSessionDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraVideoDAO;
import com.datastax.training.killrvideo.services.JsonProcessingExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Main class.
 *
 * Bootstraps the
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/killrvideo/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     * 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {

        Config config = ConfigFactory.load();

        @SuppressWarnings("unused")
        String username = config.getString(ConfigItems.CASSANDRA_USERNAME);
        @SuppressWarnings("unused")
        String password = config.getString(ConfigItems.CASSANDRA_PASSWORD);
        String keyspace = config.getString(ConfigItems.CASSANDRA_KEYSPACE);

        // create a resource config that scans for JAX-RS resources and
        // providers
        // in com.datastax.training.killrvideo.example package
        CassandraSession.initCassandra(config.getString(ConfigItems.CASSANDRA_KEYSPACE),
                config.getString(ConfigItems.CASSANDRA_USERNAME), config.getString(ConfigItems.CASSANDRA_PASSWORD),
                config.getString(ConfigItems.CASSANDRA_HOSTS).split(","));

        final ResourceConfig rc = new ResourceConfig().packages("com.datastax.training.killrvideo.services", "com.datastax.training.killrvideo.util");

        SearchSession.initSearchSession(config.getString(ConfigItems.CASSANDRA_USERNAME),
                config.getString(ConfigItems.CASSANDRA_PASSWORD), config.getString(ConfigItems.SEARCH_DC_NAME),
                keyspace, config.getString(ConfigItems.SEARCH_HOSTS).split(","));

        // Binds some concrete objects to the injectors for services
        rc.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new CassandraUserDAO()).to(UserDAO.class);
                bind(new CassandraUserSessionDAO()).to(UserSessionDAO.class);
                bind(new CassandraVideoDAO()).to(VideoDAO.class);
            }
        });

        // Allows serialization using Jackson
        rc.register(JacksonFeature.class);
        // Workaround to ensure that my exception mapper is called rather than
        // the one supplied in JacksonFeature
        rc.register(JacksonJaxbJsonProvider.class);
        rc.register(JsonProcessingExceptionMapper.class);
        rc.register(LoggingFilter.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(configureEndpoint(BASE_URI, config)),
                rc);

        // Create a handler that will server up static content
        HttpHandler baseHandler = new CLStaticHttpHandler(HttpServer.class.getClassLoader(), "/static-web/");
        server.getServerConfiguration().addHttpHandler(baseHandler, "/");

        return server;

    }

    private static String configureEndpoint(String endpoint, Config config) {

        String configuredHost = config.getString(ConfigItems.APP_LISTEN_ADDRESS);
        if (configuredHost != null) {
            Pattern hostPattern = Pattern.compile("(http://)(.*?)(/api/killrvideo)");
            Matcher m = hostPattern.matcher(endpoint);
            if (m.find()) {
                endpoint = m.replaceAll("$1" + configuredHost + "$3");
            }
        }
        return endpoint;
    }

    /**
     * Main method.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Ensure that JUL logging is handled by slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final HttpServer server = startServer();
        System.in.read();
        server.shutdownNow();
    }
}
