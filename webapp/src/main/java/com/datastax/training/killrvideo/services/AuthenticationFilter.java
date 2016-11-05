package com.datastax.training.killrvideo.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.datastax.training.killrvideo.model.UserSession;
import com.datastax.training.killrvideo.model.dao.UserSessionDAO;

/**
 * Created on 17/10/2015.
 *
 * When associated resources are protected using the @Secured annotation any
 * requests for those resources will be filtered using this implementation which
 * examines the users authtoken in the header and attempts to retrieve an
 * associated session in the database and allowing the request only if it is
 * valid.
 *
 */
@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    UserSessionDAO userSessionDAO;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Optional<String> token = Optional.ofNullable(containerRequestContext.getHeaderString("authtoken"));

        if (token.isPresent() && tokenIsValid(token.get())) {
            // Do nothing we're all good!
        } else {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    private boolean tokenIsValid(String token) {
        Optional<UserSession> userSession = Optional.ofNullable(userSessionDAO.getUserSession(UUID.fromString(token)));
        return userSession.isPresent() && userSession.get().getIsValid();
    }

}
