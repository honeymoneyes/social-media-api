package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.FeedResponse;
import com.projects.socialmediaapi.user.services.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/feed")
    public ResponseEntity<Set<FeedResponse>> performSubscribersFeed() {
        return ResponseEntity.ok(feedService.getSubscribersFeed());
    }
}
