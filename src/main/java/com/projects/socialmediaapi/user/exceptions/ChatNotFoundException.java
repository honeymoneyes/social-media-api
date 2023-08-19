package com.projects.socialmediaapi.user.exceptions;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String message) {
        super(message);
    }
}
