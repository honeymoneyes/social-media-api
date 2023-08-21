package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PageableResponse;
import com.projects.socialmediaapi.user.services.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.socialmediaapi.swagger.DescriptionConstants.FEED_TAG_DESCRIPTION;
import static com.projects.socialmediaapi.swagger.values.ExampleValues.FEED_RECEIVED_SUCCESS;
import static com.projects.socialmediaapi.swagger.values.ExampleValues.SELF_FOLLOW_UNFOLLOW_ERROR;
import static com.projects.socialmediaapi.user.constants.FeedEndpointConstants.MAIN_FEED;
import static com.projects.socialmediaapi.user.constants.FeedEndpointConstants.SHOW_FEED;

@RestController
@RequestMapping(MAIN_FEED)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Лента новостей",
        description = FEED_TAG_DESCRIPTION
)
public class FeedController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FeedService feedService;

    // -----------------------------------------------------------------------------------------------------------------

    @ApiResponse(responseCode = "200",
            description = "Лента последних постов пользователей, на которых подписан user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendShipResponse.class),
                    examples = @ExampleObject(value = FEED_RECEIVED_SUCCESS)

            ))
    @GetMapping(SHOW_FEED)
    @Operation(summary = "Лента последних постов пользователей, на которых подписан user",
    description = "Пользователь получает ленту из последних постов пользователей с возможностью" +
                  "пагинации и двунаправленной сортировки по времени опубликования. Первое значение" +
                  "отвечает за страницу по счету от 0. Второе значение отвечает за количество постов" +
                  "на странице. Третье значение отвечает за сортировку. NOT - без изменений," +
                  "DESC - по убыванию, ASC - по возрастанию. Также можно не указывать значений.")
    public ResponseEntity<?> performSubscribersFeed(@RequestParam(value = "page", required = false)
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
