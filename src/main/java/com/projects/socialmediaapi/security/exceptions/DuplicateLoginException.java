package com.projects.socialmediaapi.security.exceptions;

public class DuplicateLoginException extends RuntimeException {
    public DuplicateLoginException(String message) {
        super(message);
    }
}

