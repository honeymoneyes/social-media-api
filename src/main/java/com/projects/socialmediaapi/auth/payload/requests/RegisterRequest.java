package com.projects.socialmediaapi.auth.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Регистрационные данные")
public record RegisterRequest(
        @Schema(description = "Имя")
        @NotBlank(message = "Username shouldn't be empty")
        String username,
        @Schema(description = "email")
        @NotBlank(message = "Email shouldn't be empty")
        @Email(message = "Email address has invalid format",
                regexp = "[a-zA-Z]\\w*@\\w{3,}\\.(ru|com)")
        String email,
        @Schema(description = "Пароль")
        @NotBlank(message = "Password shouldn't be empty")
        String password) {
}
