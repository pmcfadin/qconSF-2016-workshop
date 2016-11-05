package com.datastax.training.killrvideo.testutilities;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.datastax.training.killrvideo.util.CassandraSession;
import com.datastax.training.killrvideo.util.ConfigItems;
import com.datastax.training.killrvideo.util.SearchSession;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * DataStax Academy Sample Application
 * <p/>
 * Copyright 2015 DataStax
 */
public class AbstractDSETest {

    private static final String DSE_EXECUTABLE = "dse.executable";
    private static final String DSETOOL_EXECUTABLE = "dsetool.executable";

    private static final Config config = ConfigFactory.load("test-properties");
    private static boolean schemaCreated = false;
    private static String keyspace;

    @BeforeClass()
    public static void setupCassandraUtil() throws FileNotFoundException {

        // Do this once in a single test run
        if (!schemaCreated) {

            keyspace = config.getString(ConfigItems.CASSANDRA_KEYSPACE);

            String contactPoints = config.getString(ConfigItems.CASSANDRA_HOSTS);
            String password = config.getString(ConfigItems.CASSANDRA_PASSWORD);
            String username = config.getString(ConfigItems.CASSANDRA_USERNAME);
            String dcname = config.getString(ConfigItems.SEARCH_DC_NAME);

            dropSchema(contactPoints, keyspace);
            createSchema(contactPoints, keyspace);

            // Initialise the CassandraSession objects for use
            // in the tests

            CassandraSession.initCassandra(keyspace, username, password, contactPoints);

            SearchSession.initSearchSession(username, password, dcname, keyspace, contactPoints);

            schemaCreated = true;
        }
    }

    private static void dropSchema(String contactPoint, String keyspace) throws FileNotFoundException {
        try (Cluster cluster = Cluster.builder().addContactPoint(contactPoint).build();
                Session session = cluster.connect()) {
            session.execute("DROP KEYSPACE IF EXISTS \"" + keyspace + "\"");
        }
    }

    private static void createSchema(String contactPoint, String keyspace) throws FileNotFoundException {

        // Connect to the cluster to create the keyspace
        // Query options disable refresh to speed up creation for test purposes
        try (Cluster cluster = Cluster.builder().addContactPoint(contactPoint).withQueryOptions(new QueryOptions()
                .setRefreshNodeIntervalMillis(0).setRefreshNodeListIntervalMillis(0).setRefreshSchemaIntervalMillis(0))
                .build()) {
            // Create session without a keyspace to create the schema
            try (Session session = cluster.connect()) {
                createKeyspace(session);
            }
            // Now connect to the keyspace created
            try (Session session = cluster.connect(keyspace)) {
                createSchema(session);
            }
        }

    }

    private static void createKeyspace(Session session) throws FileNotFoundException {
        executeCQL(getScannerFromResource("create-test-keyspace.cql"), session);
    }

    private static void createSchema(Session session) throws FileNotFoundException {
        executeCQL(getScannerFromFile("datamodel/cql/create-schema.cql"), session);
    }

    private static void executeCQL(Scanner statements, Session session) {
        while (statements.hasNext()) {
            String code = statements.next().trim();

            if (!code.isEmpty()) {
                session.execute(code);
            }
        }
    }

    private static Scanner getScannerFromResource(String resourceName) {
        return getScanner(AbstractDSETest.class.getClassLoader().getResourceAsStream(resourceName));
    }

    private static Scanner getScannerFromFile(String filename) throws FileNotFoundException {
        return getScanner(new FileInputStream(filename));
    }

    private static Scanner getScanner(InputStream schemaFile) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(schemaFile);
        scanner = scanner.useDelimiter(";");
        return scanner;
    }

    public static String getDSEExecutable() {
        return config.getString(DSE_EXECUTABLE);
    }

    public static String getDSEToolExecutable() {
        return config.getString(DSETOOL_EXECUTABLE);
    }

    protected static String getKeyspace() {
        return keyspace;
    }

    private static void DoCommand(String sparkCommand, String errormessage) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(sparkCommand);
        p.waitFor(20, TimeUnit.SECONDS);
        assertTrue(errormessage + "\n" + IOUtils.toString(p.getErrorStream()), 0 == p.exitValue());
    }

    protected void runSparkJob(String holding_date) throws IOException, InterruptedException {

        String dseExecutable = getDSEExecutable();

        String sparkCommand = dseExecutable + " spark-submit --class com.bigbrokers.spark.DailyBalanceSinceLast "
                + "../spark/target/scala-2.10/spark-brokers-assembly-1.0.jar keyspace=bigbrokers_test holding_date="
                + holding_date;

        DoCommand(sparkCommand, "Spark Job failed to execute:");
    }

    protected static void createDefautSolrCore(String table) throws InterruptedException, IOException {
        createDefautSolrCore(table, true);
    }

    protected static void createDefautSolrCore(String table, boolean reindex) throws InterruptedException, IOException {
        String dseToolExecutable = getDSEToolExecutable();

        String corename = mkCorename(table);
        String command = dseToolExecutable + " create_core " + corename + " generateResources=true reindex="
                + (reindex ? "true" : "false");

        DoCommand(command, "Failed to create core: " + corename);

        if (reindex) {
            Thread.sleep(10000);
        }
    }

    protected static void unload_core(String table) throws InterruptedException, IOException {
        String dseToolExecutable = getDSEToolExecutable();

        String corename = mkCorename(table);
        String command = dseToolExecutable + " unload_core " + corename;

        DoCommand(command, "Failed to unload core: " + corename);
    }

    protected static void createCustomSolrCore(String table) throws InterruptedException, IOException {
        createDefautSolrCore(table, false);

        String dseToolExecutable = getDSEToolExecutable();

        String corename = mkCorename(table);
        String schemaFile = "datamodel/solr/" + table + ".xml";

        String command = dseToolExecutable + " reload_core " + corename + " schema=" + schemaFile + " reindex=true";

        DoCommand(command, "Failed to reload core: " + corename);

        Thread.sleep(10000);
    }

    private static String mkCorename(String table) {
        return keyspace + "." + table;
    }
}
