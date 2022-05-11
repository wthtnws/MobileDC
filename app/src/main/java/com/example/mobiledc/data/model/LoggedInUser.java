package com.example.mobiledc.data.model;

import androidx.annotation.Nullable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private String apiToken;

    public LoggedInUser(String username, @Nullable String apiToken) {
        this.username = username;
        this.username = apiToken;
    }

    public String getUsername() {
        return username;
    }

    private String getapiToken() {
        return apiToken;
    }
}