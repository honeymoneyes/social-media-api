package com.projects.socialmediaapi.post.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private String fileName;
    private String downloadUri;
    private String contentType;
    private Long size;
}
