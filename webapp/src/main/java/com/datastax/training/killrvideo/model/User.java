package com.datastax.training.killrvideo.model;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created on 17/10/2015.
 */

@JsonIgnoreProperties({ "password", "salt" })
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private Date joined;
    private ByteBuffer password;
    private ByteBuffer salt;
    private UUID userId;
    private Map<String, BigDecimal> phoneNumbers;
    private Map<String, Address> addresses;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public ByteBuffer getPassword() {
        return password;
    }

    public void setPassword(ByteBuffer password) {
        this.password = password;
    }

    public ByteBuffer getSalt() {
        return salt;
    }

    public void setSalt(ByteBuffer salt) {
        this.salt = salt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, BigDecimal> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Map<String, BigDecimal> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

}