package com.projects.socialmediaapi.post.payload.responses;

import com.projects.socialmediaapi.post.models.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<Post> posts;
}
