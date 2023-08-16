package com.projects.socialmediaapi.auth.payload.requests;

public record LoginRequest(
        String email,
        String password) {
}
