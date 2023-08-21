package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.requests.PostRequest;
import com.projects.socialmediaapi.user.payload.responses.*;
import com.projects.socialmediaapi.user.services.ImageService;
import com.projects.socialmediaapi.user.services.PostService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.projects.socialmediaapi.swagger.values.ExampleValues.JWT_TOKEN_EXPIRED;
import static com.projects.socialmediaapi.user.constants.PostEndpointConstants.*;

@RestController
@RequestMapping(MAIN_POSTS)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
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

    @PostMapping(CREATE_POST)
    public ResponseEntity<UploadPostResponse> performCreatePost(@Valid
                                                                @ModelAttribute
                                                                PostRequest request)  {
        return ResponseEntity.ok(postService.createPost(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PatchMapping(UPDATE_POST)
    public ResponseEntity<UpdatePostResponse> performUpdatePost(@Valid
                                                                @ModelAttribute PostRequest request,
                                                                @PathVariable("postId") Long id) throws IOException {
        return ResponseEntity.ok(postService.updatePost(request, id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @DeleteMapping(DELETE_POST)
    public ResponseEntity<DeletePostResponse> performDeletePost(@PathVariable("postId") Long id) {
        return ResponseEntity.ok(postService.deletePost(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
