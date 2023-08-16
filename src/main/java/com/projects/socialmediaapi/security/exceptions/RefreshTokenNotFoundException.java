package com.projects.socialmediaapi.security.exceptions;


public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String token, String message) {
        super(String.format("Failed for [%s]: %s ", token, message));
    }
}
