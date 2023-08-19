package com.projects.socialmediaapi.user.exceptions;

public class MessageSendingNotAllowedException extends RuntimeException {
    public MessageSendingNotAllowedException(String message) {
        super(message);
    }
}
