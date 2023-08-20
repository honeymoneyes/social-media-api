package com.projects.socialmediaapi.user.exceptions;

public class FriendAlreadyExistException extends RuntimeException {
    public FriendAlreadyExistException(String message) {
        super(message);
    }
}
