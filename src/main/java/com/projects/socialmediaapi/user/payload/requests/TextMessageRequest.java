package com.projects.socialmediaapi.user.payload.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageRequest {
    @NotBlank(message = "Message shouldn't be empty")
    private String text;
}
