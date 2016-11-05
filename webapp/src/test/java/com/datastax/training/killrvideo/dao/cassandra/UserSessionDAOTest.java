package com.datastax.training.killrvideo.dao.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserSessionDAO;
import org.junit.Test;

import com.datastax.training.killrvideo.model.UserSession;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;
import com.datastax.driver.core.utils.UUIDs;

public class UserSessionDAOTest extends AbstractDSETest {

    @Test
    public void testReadWriteSession() {
        CassandraUserSessionDAO userSessionDAO = new CassandraUserSessionDAO();

        UUID userId = UUIDs.random();
        UUID sessionToken = UUIDs.timeBased();

        UserSession writeSession = new UserSession(userId, sessionToken);

        userSessionDAO.addSession(writeSession, 3600);

        UserSession readSession = userSessionDAO.getUserSession(sessionToken);

        assertEquals(userId, readSession.getUserId());
        assertEquals(sessionToken, readSession.getSessionToken());
        assertTrue(readSession.getIsValid());

    }

    @Test
    public void testSessionTTLsAfterAWhile() {
        CassandraUserSessionDAO userSessionDAO = new CassandraUserSessionDAO();

        UUID userId = UUIDs.random();
        UUID sessionToken = UUIDs.timeBased();

        UserSession writeSession = new UserSession(userId, sessionToken);

        userSessionDAO.addSession(writeSession, 5);

        UserSession readSession = userSessionDAO.getUserSession(sessionToken);

        // Validate the session is there
        assertEquals(sessionToken, readSession.getSessionToken());

        // Sleep for 6 Seconds
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            fail("Test Interrupted");
        }

        // Read it again
        UserSession readSession2 = userSessionDAO.getUserSession(sessionToken);
        assertNull(readSession2);

    }
}
