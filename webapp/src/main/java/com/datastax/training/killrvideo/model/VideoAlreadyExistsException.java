package com.datastax.training.killrvideo.model;

/**
 * Created on 09/11/2015.
 */
public class VideoAlreadyExistsException extends Exception {
    private static final long serialVersionUID = 1L;

    public VideoAlreadyExistsException(String msg) {
        super(msg);
    }

}
