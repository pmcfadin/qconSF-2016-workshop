package com.datastax.training.killrvideo.model;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "user_session", readConsistency = "LOCAL_ONE", writeConsistency = "LOCAL_QUORUM")
public class UserSession {

    @PartitionKey
    @Column(name = "session_token")
    private UUID sessionToken;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "is_valid")
    private Boolean isValid;

    public UserSession() {
    };

    public UserSession(UUID userId, UUID sessionToken) {
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.setIsValid(true);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(UUID sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
}
