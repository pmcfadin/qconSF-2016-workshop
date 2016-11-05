package com.datastax.training.killrvideo.services;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.training.killrvideo.model.RegisteringUser;
import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.model.UserAlreadyExistsException;
import com.datastax.training.killrvideo.model.dao.UserDAO;
import com.datastax.training.killrvideo.util.AuthenticationUtils;

/**
 * Created on 05/10/2015.
 */
@Path("users")
@Singleton
public class UserService {

    private UserDAO userDAO;

    @Inject
    public UserService(UserDAO dao) {
        this.userDAO = dao;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(RegisteringUser user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Response response;

        try {
            userDAO.addUser(augmentUser(user));
            userDAO.addAddressToUser(user.getEmail(), "default", user.getAddress());
            // Clear password so it's not serialized on return...
            user.setClearTextPassword(null);
            response = Response.status(Response.Status.CREATED).entity(user).build();
        } catch (UserAlreadyExistsException uae) {
            response = Response.status(Response.Status.CONFLICT).build();
        }
        return response;
    }

    /**
     * Retrieve the user by email address. This is the only place where we would
     * use this everywhere else we should use the userId
     */
    @GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response get(@PathParam("email") String emailAddress) {
        Response response;
        User user = userDAO.getUser(emailAddress);
        if (user == null) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } else {
            response = Response.ok().entity(user).build();
        }
        return response;
    }

    /**
     * Hash the clear text password and add a salt to it so it can be persisted
     * 
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private User augmentUser(RegisteringUser user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Retrieve a random salt to be used when hashing an incoming password
        byte[] salt = AuthenticationUtils.getRandomSalt();
        // Hash the password
        byte[] hashPwd = AuthenticationUtils.hash(user.getClearTextPassword(), salt);
        user.setPassword(ByteBuffer.wrap(hashPwd));
        user.setSalt(ByteBuffer.wrap(salt));
        user.setJoined(new Date());
        user.setUserId(UUIDs.timeBased());
        return user;
    }
}
