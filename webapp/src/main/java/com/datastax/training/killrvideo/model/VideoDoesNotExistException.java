package com.datastax.training.killrvideo.model;

/**
 * Created on 09/11/2015.
 */
public class VideoDoesNotExistException extends Exception {
    private static final long serialVersionUID = 1L;

    public VideoDoesNotExistException(String msg) {
        super(msg);
    }

}
