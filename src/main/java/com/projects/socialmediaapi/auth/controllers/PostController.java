package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.post.payload.requests.PostRequest;
import com.projects.socialmediaapi.post.payload.responses.DeletePostResponse;
import com.projects.socialmediaapi.post.payload.responses.PostResponse;
import com.projects.socialmediaapi.post.payload.responses.UpdatePostResponse;
import com.projects.socialmediaapi.post.payload.responses.UploadPostResponse;
import com.projects.socialmediaapi.post.services.ImageService;
import com.projects.socialmediaapi.post.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.projects.socialmediaapi.post.constants.EndpointConstants.*;

@RestController
@RequestMapping(MAIN)
@RequiredArgsConstructor
public class PostController {

    // -----------------------------------------------------------------------------------------------------------------

    private final PostService postService;
    private final ImageService imageService;

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_ALL_POSTS)
    public ResponseEntity<PostResponse> performShowAllPosts(@PathVariable("userId") Long postId) {
        return ResponseEntity.ok(postService.showAllPostsByUserId(postId));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_IMAGE_OF_POST)
    public ResponseEntity<Resource> performShowImage(@PathVariable("postId") Long postId) {
        return imageService.showImageByPostId(postId);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(CREATE)
    public ResponseEntity<UploadPostResponse> performCreatePost(@Valid
                                                                @ModelAttribute
                                                                    PostRequest request) throws IOException {
        return ResponseEntity.ok(postService.createPost(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PatchMapping(UPDATE)
    public ResponseEntity<UpdatePostResponse> performUpdatePost(@Valid
                                                                @ModelAttribute PostRequest request,
                                                                @PathVariable("postId") Long id) throws IOException {
        return ResponseEntity.ok(postService.updatePost(request, id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @DeleteMapping(DELETE)
    public ResponseEntity<DeletePostResponse> performDeletePost(@PathVariable("postId") Long id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
