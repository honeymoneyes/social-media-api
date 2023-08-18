package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.post.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.socialmediaapi.post.constants.EndpointConstants.POST;
import static com.projects.socialmediaapi.post.constants.EndpointConstants.SHOW_IMAGE_OF_POST;

@RestController
@RequiredArgsConstructor
@RequestMapping(POST)
public class ImageController {

    private final ImageService imageService;

    @GetMapping(SHOW_IMAGE_OF_POST)
    public ResponseEntity<Resource> performShowImage(@PathVariable("postId") Long postId) {
        return imageService.showImageByPostId(postId);
    }
}
