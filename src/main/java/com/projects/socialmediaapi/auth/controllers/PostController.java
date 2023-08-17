package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.post.payload.requests.PostRequest;
import com.projects.socialmediaapi.post.payload.responses.PostResponse;
import com.projects.socialmediaapi.post.payload.responses.UploadPostResponse;
import com.projects.socialmediaapi.post.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.projects.socialmediaapi.post.constants.EndpointConstants.*;

@RestController
@RequestMapping(MAIN)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(CREATE)
    public ResponseEntity<UploadPostResponse> performCreatePost(@Valid
                                                                @ModelAttribute PostRequest request) throws IOException {
        return ResponseEntity.ok(postService.createPost(request));
    }

    @GetMapping("/show_image/{postId}")
    public ResponseEntity<Resource> performShowImage(@PathVariable("postId") Long postId) throws IOException {
        return postService.showImageByPostId(postId);
    }

    @GetMapping(SHOW_ALL)
    public ResponseEntity<PostResponse> performShowAllPosts(@PathVariable("userId") Long postId) throws IOException {
        return ResponseEntity.ok(postService.showAllPostsByUserId(postId));
    }


//    @PatchMapping(UPDATE)
//    public ResponseEntity<?> performUpdatePost() {
//        return ResponseEntity.ok();
//    }
//
//    @DeleteMapping(DELETE)
//    public ResponseEntity<?> performDeletePost() {
//        return ResponseEntity.ok();
//    }
}
