package com.projects.socialmediaapi.auth.payload.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Username shouldn't be empty")
        String username,
        @NotBlank(message = "Email shouldn't be empty")
        @Email(message = "Email address has invalid format",
                regexp = "[a-zA-Z]\\w*@\\w{3,}\\.(ru|com)")
        String email,
        @NotBlank(message = "Password shouldn't be empty")
        String password) {
}
