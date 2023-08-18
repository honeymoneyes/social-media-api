package com.projects.socialmediaapi.post.exceptions;

public class UnauthorizedPostUpdatedException extends RuntimeException {
    public UnauthorizedPostUpdatedException(String message) {
        super(message);
    }
}
