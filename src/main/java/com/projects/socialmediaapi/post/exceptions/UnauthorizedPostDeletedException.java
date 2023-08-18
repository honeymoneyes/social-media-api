package com.projects.socialmediaapi.post.exceptions;

public class UnauthorizedPostDeletedException extends RuntimeException {
    public UnauthorizedPostDeletedException(String message) {
        super(message);
    }
}
