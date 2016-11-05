package com.datastax.training.killrvideo.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.training.killrvideo.model.User;
import com.datastax.training.killrvideo.model.UserSession;
import com.datastax.training.killrvideo.model.dao.UserDAO;
import com.datastax.training.killrvideo.model.dao.UserSessionDAO;
import com.datastax.training.killrvideo.util.AuthenticationUtils;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Created on 17/10/2015.
 */
@Path("authenticate")
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserDAO userDAO;
    private final UserSessionDAO userSessionDAO;
    public final int SESSION_TTL = 3600;

    @Inject
    public AuthenticationService(UserDAO userDAO, UserSessionDAO userSessionDAO) {
        this.userDAO = userDAO;
        this.userSessionDAO = userSessionDAO;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username, @FormParam("password") String password)
            throws IOException, NoSuchAlgorithmException {

        Response response = Response.status(Response.Status.FORBIDDEN).build();

        log.debug("authentication received -> username : {}", username);

        User user = userDAO.getUser(username);
        if (user != null) {
            byte[] salt = user.getSalt().array();
            byte[] hashedPassword = AuthenticationUtils.hash(password, salt);
            if (Arrays.equals(user.getPassword().array(), hashedPassword)) {
                log.info("login successful -> username : {}", username);
                UserSession newSession = createSession(user.getUserId());
                userSessionDAO.addSession(newSession, SESSION_TTL);
                response = Response.ok().header("authtoken", newSession.getSessionToken().toString()).entity(user)
                        .build();
            }
        }

        return response;
    }

    private UserSession createSession(UUID userId) {
        return new UserSession(userId, UUIDs.timeBased());
    }

}
