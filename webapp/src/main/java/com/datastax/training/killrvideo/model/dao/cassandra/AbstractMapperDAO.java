package com.datastax.training.killrvideo.model.dao.cassandra;

import java.lang.reflect.ParameterizedType;

import com.datastax.training.killrvideo.util.CassandraSession;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.extras.codecs.enums.EnumOrdinalCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;

/**
 * Created on 22/10/2015.
 */
public abstract class AbstractMapperDAO<T> extends CassandraDAO {

    @SuppressWarnings("unchecked")
    private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];

    protected final Mapper<T> mapper;

    protected AbstractMapperDAO() {
        this.mapper = CassandraSession.getMappingManager(clazz);

    }

    protected Result<T> executeAndMapResults(String statementString) {
        return executeAndMapResults(new SimpleStatement(statementString));
    }

    protected Result<T> executeAndMapResults(Statement statement) {
        return mapper.map(getCassandraSession().execute(statement));
    }

}
