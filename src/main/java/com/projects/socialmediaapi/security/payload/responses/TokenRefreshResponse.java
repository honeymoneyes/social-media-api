package com.projects.socialmediaapi.security.payload.responses;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
