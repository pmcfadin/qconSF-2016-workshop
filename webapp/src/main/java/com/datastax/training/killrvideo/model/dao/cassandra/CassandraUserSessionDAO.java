package com.datastax.training.killrvideo.model.dao.cassandra;

import java.util.UUID;

import javax.inject.Singleton;

import com.datastax.training.killrvideo.model.UserSession;
import com.datastax.training.killrvideo.model.dao.UserSessionDAO;
import com.datastax.driver.mapping.Mapper;

@Singleton
public class CassandraUserSessionDAO extends AbstractMapperDAO<UserSession> implements UserSessionDAO {

    public CassandraUserSessionDAO() {
        super();
    }

    @Override
    public void addSession(UserSession session, int ttl) {
        mapper.save(session, Mapper.Option.ttl(ttl));
    }

    @Override
    public UserSession getUserSession(UUID token) {
        return mapper.get(token);
    }

}
