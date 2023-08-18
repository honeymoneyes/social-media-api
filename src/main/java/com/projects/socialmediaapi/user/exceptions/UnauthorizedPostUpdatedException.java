package com.projects.socialmediaapi.user.exceptions;

public class UnauthorizedPostUpdatedException extends RuntimeException {
    public UnauthorizedPostUpdatedException(String message) {
        super(message);
    }
}
