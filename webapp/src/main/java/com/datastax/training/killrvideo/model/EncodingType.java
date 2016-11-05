package com.datastax.training.killrvideo.model;

import java.util.Set;

/**
 * Created on 26/10/2015. Container for Address UDT
 */
public class EncodingType {
    private String encoding;
    private int height;
    private int width;
    private Set<Integer> bitrates;

    public EncodingType() {
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Set<Integer> getBitrates() {
        return bitrates;
    }

    public void setBitrates(Set<Integer> bitrates) {
        this.bitrates = bitrates;
    }

}
