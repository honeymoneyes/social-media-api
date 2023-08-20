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
public class PostInfo {
    private String title;
    private String body;
    private LocalDateTime timestamp;
    private ImageInfo imageInfo;
}
