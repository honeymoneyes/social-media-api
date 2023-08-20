package com.projects.socialmediaapi.user.payload.responses;

import com.projects.socialmediaapi.user.models.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {
    private String username;
    private Post lastPost;
}
