package com.projects.socialmediaapi.auth.payload.requests;

public record RegisterRequest(
        String username,
        String email,
        String password) {
}
