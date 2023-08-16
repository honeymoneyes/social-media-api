package com.projects.socialmediaapi.auth.payload.requests;

public record TokenRefreshRequest(
        String refreshToken
) {
}
