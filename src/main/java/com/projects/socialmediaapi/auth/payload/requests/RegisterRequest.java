package com.projects.socialmediaapi.auth.payload.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Регистрационные данные")
public record RegisterRequest(
        @Schema(description = "Имя")
        @NotBlank(message = "Username cannot be empty or contain only spaces")
        String username,
        @Schema(description = "email")
        @NotBlank(message = "Email cannot be empty or contain only spaces")
        @Email(message = "Email address has invalid format",
                regexp = "[a-zA-Z]\\w*@\\w{3,}\\.(ru|com)")
        String email,
        @Schema(description = "Пароль")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$",
                message = "Password must contain at least one letter and one digit"
        )
        @NotBlank(message = "Password cannot be empty or contain only spaces")
        @Size(min = 8, max = 20, message = "Password must be between {min} and {max} characters")
        String password) {
}
