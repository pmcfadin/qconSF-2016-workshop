package com.datastax.training.killrvideo.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 20/10/2015.
 *
 * This class is required to ensure that exceptions get logged otherwise they
 * are swallowed
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable t) {
        log.error("EXCEPTION", t);
        return Response.serverError().build();
    }

}