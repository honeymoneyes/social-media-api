package com.projects.socialmediaapi.user.exceptions;

public class SubscriberAlreadyExistException extends RuntimeException {
    public SubscriberAlreadyExistException(String message) {
        super(message);
    }
}
