package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.PageableResponse;
import com.projects.socialmediaapi.user.services.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.socialmediaapi.user.constants.FeedEndpointConstants.MAIN_FEED;
import static com.projects.socialmediaapi.user.constants.FeedEndpointConstants.SHOW_FEED;

@RestController
@RequestMapping(MAIN_FEED)
@RequiredArgsConstructor
public class FeedController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FeedService feedService;

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_FEED)
    public ResponseEntity<PageableResponse> performSubscribersFeed(@RequestParam(value = "page", required = false)
                                                                       Long page,
                                                                   @RequestParam(value = "posts_per_page", required =
                                                                            false) Long postsPerPage,
                                                                   @RequestParam(value = "sort_by_timestamp",
                                                                            defaultValue = "NOT")
                                                                       String sortByTimestamp) {
        return ResponseEntity.ok(feedService.getSubscribersFeed(page, postsPerPage, sortByTimestamp));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
