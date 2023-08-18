package com.projects.socialmediaapi.post.payload.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotBlank(message = "Title shouldn't be empty")
    private String title;
    @NotBlank(message = "Body shouldn't be empty")
    private String body;
    private MultipartFile image;
}
