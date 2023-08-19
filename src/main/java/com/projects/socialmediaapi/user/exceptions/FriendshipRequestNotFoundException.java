package com.projects.socialmediaapi.user.exceptions;

public class FriendshipRequestNotFoundException extends RuntimeException {
    public FriendshipRequestNotFoundException(String message) {
        super(message);
    }
}
