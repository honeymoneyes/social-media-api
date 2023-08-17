package com.projects.socialmediaapi.security.payload.requests;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "Refresh token shouldn't be empty!")
        String refreshToken
) {
}
