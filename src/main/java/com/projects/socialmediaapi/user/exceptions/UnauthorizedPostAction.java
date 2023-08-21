package com.projects.socialmediaapi.user.exceptions;

public class UnauthorizedPostAction extends RuntimeException {
    public UnauthorizedPostAction(String message) {
        super(message);
    }
}
