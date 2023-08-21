package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.payload.responses.PageableResponse;

public interface FeedService {
    PageableResponse getSubscribersFeed(Long page, Long postsPerPage, String sortByTimestamp);
}
