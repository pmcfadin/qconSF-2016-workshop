package com.datastax.training.killrvideo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response;

import com.datastax.training.killrvideo.model.RegisteringUser;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserSessionDAO;
import org.junit.Test;

import com.datastax.training.killrvideo.model.dao.UserDAO;
import com.datastax.training.killrvideo.model.dao.cassandra.CassandraUserDAO;
import com.datastax.training.killrvideo.testutilities.AbstractDSETest;

public class UserAuthenticationTest extends AbstractDSETest {

    @Test
    public void createUserAndSuccessfullyAuthenticate() throws IOException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();

        RegisteringUser user = new RegisteringUser();
        user.setEmail("newuser2@datastax.com");
        user.setFirstName("New");
        user.setLastName("User");
        user.setClearTextPassword("P@ssw0rd");

        UserService us = new UserService(userDAO);
        Response response = us.create(user);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Now log the user in
        AuthenticationService as = new AuthenticationService(userDAO, new CassandraUserSessionDAO());
        response = as.login("newuser2@datastax.com", "P@ssw0rd");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getHeaderString("authtoken"));
    }

    @Test
    public void createUserAndFailAuthentication() throws IOException, NoSuchAlgorithmException {

        UserDAO userDAO = new CassandraUserDAO();

        RegisteringUser user = new RegisteringUser();
        user.setEmail("newuser3@datastax.com");
        user.setFirstName("New");
        user.setLastName("User");
        user.setClearTextPassword("P@ssw0rd");

        UserService us = new UserService(userDAO);
        Response response = us.create(user);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        // Now log the user in
        AuthenticationService as = new AuthenticationService(userDAO, new CassandraUserSessionDAO());
        response = as.login("newuser3@datastax.com", "I Forgot");

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}
