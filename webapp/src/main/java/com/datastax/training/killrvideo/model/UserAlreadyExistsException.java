package com.datastax.training.killrvideo.model;

/**
 * Created on 09/11/2015.
 */
public class UserAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

}
