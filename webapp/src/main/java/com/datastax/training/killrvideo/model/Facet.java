package com.datastax.training.killrvideo.model;

import java.util.Map;

import com.google.gson.JsonElement;

/**
 * DataStax Academy Sample Application
 * <p/>
 * Copyright 2015 DataStax
 */

public class Facet {
    String term;
    int quantity;

    public Facet(String value, int quantity) {
        this.term = value;
        this.quantity = quantity;
    }

    public Facet(Map.Entry<String, JsonElement> entry) {
        term = entry.getKey();
        quantity = entry.getValue().getAsInt();
    }

    public String getTerm() {
        return term;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Facet(" + term + "," + quantity + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Facet t = (Facet) obj;
        return term.contentEquals(t.term) && (quantity == t.quantity);
    }
}
