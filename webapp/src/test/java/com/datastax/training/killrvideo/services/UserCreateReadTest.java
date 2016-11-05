package com.datastax.training.killrvideo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import com.datastax.training.killrvideo.model.RegisteringUser;
import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.testutilities.TestData;
import com.datastax.training.killrvideo.util.CassandraSession;
import org.junit.BeforeClass;
import org.junit.Test;

import com.datastax.training.killrvideo.model.Address;
import com.datastax.training.killrvideo.model.dao.UserDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserDAO;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;

/**
 * Created on 05/01/2016.
 */
public class UserCreateReadTest extends AbstractDSETest {

    @Test
    public void createUserWithAllFields() throws IOException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();
        UserService us = new UserService(userDAO);

        Address useraddress = new Address();
        useraddress.setStreet("123 Main Street");
        useraddress.setCity("Springfield");
        useraddress.setCountry("USA");
        useraddress.setPostalCode("12345");

        RegisteringUser user = new RegisteringUser();
        user.setEmail("newuser@datastax.com");
        user.setFirstName("New");
        user.setLastName("User");
        user.setClearTextPassword("P@ssw0rd");

        Response response = us.create(user);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void checkDuplicateUserCreationFails() throws IOException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();
        UserService us = new UserService(userDAO);

        RegisteringUser user = createUser("newuser@foobar.com");

        RegisteringUser duplicateUser = createUser("newuser@foobar.com");

        Response response = us.create(user);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        response = us.create(duplicateUser); // Attempt to create the user again

        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());

    }

    private RegisteringUser createUser(String emailAddress) {
        RegisteringUser user = new RegisteringUser();
        user.setEmail(emailAddress);
        user.setFirstName("New");
        user.setLastName("User");
        user.setClearTextPassword("P@ssw0rd");
        return user;
    }

    @BeforeClass
    public static void purge() {
        CassandraSession.getSession().execute("TRUNCATE USER");
    }

    @Test
    public void fetchUserThatDoesNotExist() throws IOException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();
        UserService us = new UserService(userDAO);

        Response response = us.get("foo@foobar.com");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void createUserAndFetch() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();
        UserService us = new UserService(userDAO);

        String testEmailAddress = TestData.REGISTERING_USER1.getEmail();

        us.create(TestData.REGISTERING_USER1);

        Response response = us.get(testEmailAddress);
        User retrieved = ((User) response.getEntity());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(retrieved);
        assertEquals(retrieved.getFirstName(), TestData.REGISTERING_USER1.getFirstName());
        assertEquals(retrieved.getLastName(), TestData.REGISTERING_USER1.getLastName());
        assertEquals(retrieved.getEmail(), TestData.REGISTERING_USER1.getEmail());

    }
}
