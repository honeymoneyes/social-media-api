package com.projects.socialmediaapi.user.payload.responses;

import com.projects.socialmediaapi.user.models.Post;
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
