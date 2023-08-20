package com.projects.socialmediaapi.user.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponse {
    private Long totalItems;
    private List<FeedResponse> feed;
    private int totalPages;
    private int currentPage;
}
