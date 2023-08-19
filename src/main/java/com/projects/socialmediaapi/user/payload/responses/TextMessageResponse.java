package com.projects.socialmediaapi.user.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageResponse {
    private String senderUsername;
    private String receiverUsername;
    private String text;
    private LocalDateTime timestamp;
}
