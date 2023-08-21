package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.payload.responses.DeletePostResponse;
import com.projects.socialmediaapi.user.payload.responses.PostResponse;
import com.projects.socialmediaapi.user.payload.responses.UpdatePostResponse;
import com.projects.socialmediaapi.user.payload.responses.UploadPostResponse;

public interface PostService {

    UploadPostResponse createPost(PostRequest request);

    UpdatePostResponse updatePost(PostRequest request, Long id);

    DeletePostResponse deletePost(Long id);

    PostResponse showAllPostsByUserId(Long postId);
}
