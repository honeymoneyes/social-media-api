package com.projects.socialmediaapi.security.exceptions;

public class RefreshTokenExpirationException extends RuntimeException {
    public RefreshTokenExpirationException(String message) {
        super(message);
    }
}
