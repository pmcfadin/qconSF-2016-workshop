package com.datastax.training.killrvideo.model.dao;

import java.util.UUID;

import com.datastax.training.killrvideo.model.UserSession;

/**
 * Created on 20/10/2015.
 */
public interface UserSessionDAO {

    void addSession(UserSession userSession, int ttl);

    UserSession getUserSession(UUID token);

}
