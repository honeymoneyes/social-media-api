package com.projects.socialmediaapi.user.exceptions;

public class ChatNotAllowedException extends RuntimeException {
    public ChatNotAllowedException(String message) {
        super(message);
    }
}
