package com.datastax.training.killrvideo.dao.cassandra;

import static org.junit.Assert.assertNotNull;

import com.datastax.training.killrvideo.model.UserAlreadyExistsException;
import com.datastax.training.killrvideo.testutilities.TestData;
import com.datastax.training.killrvideo.util.CassandraSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserDAO;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;
import com.datastax.driver.core.Session;

/**
 * Created on 19/10/2015.
 */
@RunWith(JUnit4.class)
public class UserDAOTest extends AbstractDSETest {

    @Before
    public void cleanupTransactionTables() {
        Session session = CassandraSession.getSession();
        session.execute("TRUNCATE user");
    }

    @Test
    public void testAddUserAndRetrieveSameUser() throws UserAlreadyExistsException {
        CassandraUserDAO userDAO = new CassandraUserDAO();

        userDAO.addUser(TestData.TEST_USER1);
        userDAO.addAddressToUser(TestData.TEST_USER1.getEmail(), "Home", TestData.TEST_ADDRESS1);

        User savedUser = userDAO.getUser(TestData.TEST_USER1.getEmail());

        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());

    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testAddSameUserTwiceThrowsException() throws UserAlreadyExistsException {

        CassandraUserDAO userDAO = new CassandraUserDAO();

        userDAO.addUser(TestData.TEST_USER2);

        userDAO.addUser(TestData.TEST_USER2);

    }

}
