package com.projects.socialmediaapi.user.exceptions;

public class UnauthorizedPostDeletedException extends RuntimeException {
    public UnauthorizedPostDeletedException(String message) {
        super(message);
    }
}
