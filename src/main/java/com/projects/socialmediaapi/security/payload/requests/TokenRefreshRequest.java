package com.projects.socialmediaapi.security.payload.requests;

public record TokenRefreshRequest(
        String refreshToken
) {
}
