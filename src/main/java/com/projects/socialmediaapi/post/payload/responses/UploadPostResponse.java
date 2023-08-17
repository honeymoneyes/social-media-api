package com.projects.socialmediaapi.post.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadPostResponse {
    private String title;
    private String body;
    private ImageResponse imageResponse;
}
