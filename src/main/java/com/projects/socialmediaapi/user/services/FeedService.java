package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.exceptions.SubscriberNotFoundException;
import com.projects.socialmediaapi.user.models.PageImpl;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.models.Post;
import com.projects.socialmediaapi.user.payload.responses.FeedResponse;
import com.projects.socialmediaapi.user.payload.responses.ImageInfo;
import com.projects.socialmediaapi.user.payload.responses.PageableResponse;
import com.projects.socialmediaapi.user.payload.responses.PostInfo;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.projects.socialmediaapi.user.constants.PostConstants.OUT_OF_RANGE;
import static com.projects.socialmediaapi.user.constants.UserConstants.POST_PER_PAGE_IS_NOT_POSITIVE;
import static com.projects.socialmediaapi.user.constants.UserConstants.SUBSCRIPTIONS_NOT_FOUND;
import static com.projects.socialmediaapi.user.enums.SortStatus.ASC;
import static com.projects.socialmediaapi.user.enums.SortStatus.DESC;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getTimestamp;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FeedService {

    // -----------------------------------------------------------------------------------------------------------------

    private final UserInteractionService userInteractionService;
    private final PersonRepository personRepository;

    // -----------------------------------------------------------------------------------------------------------------

    public PageableResponse getSubscribersFeed(Long page, Long postsPerPage, String sortByTimestamp) {

        Person person = userInteractionService.getLoggedUser();

        List<Person> listSubscriptions = getSubscriptions(person);

        List<Post> listLatestPostSubscriptions = getListLatestPostSubscriptions(listSubscriptions);

        List<Post> sortListLatestPostSubscriptions =
                sortListLatestPostSubscriptions(listLatestPostSubscriptions, sortByTimestamp);
        List<FeedResponse> latestPostFeedResponse =
                getLatestPostFeedResponse(sortListLatestPostSubscriptions);

        if (page == null || postsPerPage == null) {
            return getPageableResponseIfParamsAreNull(latestPostFeedResponse);
        }


        PageImpl<FeedResponse> content = getPage(
                page,
                postsPerPage,
                sortListLatestPostSubscriptions,
                latestPostFeedResponse);

        return getPageableResponse(content);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Create pagination and extract data for the current page*/
    private PageImpl<FeedResponse> getPage(Long page, Long postsPerPage,
                                           List<Post> sortListLatestPostSubscriptions,
                                           List<FeedResponse> latestPostFeedResponse) {

        PageRequest pageable = getPageRequest(page, postsPerPage);

        int totalPages = getTotalPages(postsPerPage, sortListLatestPostSubscriptions.size());

        if (page >= totalPages) {
            // TODO проверить >=
            throw new IllegalArgumentException(OUT_OF_RANGE);
        }

        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), latestPostFeedResponse.size());

        List<FeedResponse> feedResponses = latestPostFeedResponse.subList(fromIndex, toIndex);

        return getFeedResponsePage(latestPostFeedResponse, pageable, feedResponses);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Calculate the total number of pages*/
    private int getTotalPages(Long postsPerPage, int totalPosts) {
        if (postsPerPage <= 0) {
            throw new IllegalArgumentException(POST_PER_PAGE_IS_NOT_POSITIVE);
        }
        return (int) Math.ceil((double) totalPosts / postsPerPage);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Sort the list of latest posts*/
    private List<Post> sortListLatestPostSubscriptions(List<Post> listLatestPostSubscriptions,
                                                       String sortByTimestamp) {
        if (ASC(sortByTimestamp)) {
            return getListAscendingSortedLatestPost(listLatestPostSubscriptions);
        } else if (DESC(sortByTimestamp)) {
            return getListDescendingSortedLatestPost(listLatestPostSubscriptions);
        } else {
            return listLatestPostSubscriptions;
        }
    }
    // -----------------------------------------------------------------------------------------------------------------

    /*Sorting by descending order is used here*/
    private static List<Post> getListDescendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .toList();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Sorting by ascending order is used here*/
    private static List<Post> getListAscendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp))
                .toList();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Convert the list of posts to a list of FeedResponse objects*/
    private static List<FeedResponse> getLatestPostFeedResponse(List<Post> setLatestPostSubscriptions) {
        return setLatestPostSubscriptions
                .stream()
                .map(FeedService::getFeedResponse)
                .collect(toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get the latest posts from subscriptions*/
    private static List<Post> getListLatestPostSubscriptions(List<Person> listSubscriptions) {
        return listSubscriptions
                .stream()
                .map(Person::getPosts)
                .map(findPostWithLatestTimestamp())
                .filter(Objects::nonNull)
                .collect(toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Function<List<Post>, Post> findPostWithLatestTimestamp() {
        return post -> post.stream()
                .max(Comparator.comparing(Post::getTimestamp))
                .orElse(null);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get the list of user's subscriptions*/
    private List<Person> getSubscriptions(Person person) {
        return personRepository
                .findBySubscribersContaining(person)
                .orElseThrow(() ->
                        new SubscriberNotFoundException(SUBSCRIPTIONS_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PageableResponse getPageableResponse(PageImpl<FeedResponse> content) {
        return PageableResponse.builder()
                .totalPages(content.getTotalPages())
                .feed(content.getContent())
                .totalItems(content.getTotalElements())
                .currentPage(content.getNumber())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PageableResponse getPageableResponseIfParamsAreNull(List<FeedResponse> content) {
        return PageableResponse.builder()
                .totalPages(0)
                .feed(content)
                .totalItems(0L)
                .currentPage(0)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PageRequest getPageRequest(Long page, Long postsPerPage) {
        return PageRequest.of(page.intValue(), postsPerPage.intValue());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PageImpl<FeedResponse> getFeedResponsePage(List<FeedResponse> latestPostFeedResponse,
                                                              PageRequest pageable, List<FeedResponse> feedResponses) {
        return new PageImpl<>(feedResponses, pageable,
                latestPostFeedResponse.size());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean DESC(String sortByTimestamp) {
        return sortByTimestamp.equals(DESC.name());
    }
    // -----------------------------------------------------------------------------------------------------------------

    private static boolean ASC(String sortByTimestamp) {
        return sortByTimestamp.equals(ASC.name());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PostInfo getPostInfo(Post post) {
        return PostInfo.builder()
                .title(post.getTitle())
                .body(post.getBody())
                .timestamp(getTimestamp(post.getTimestamp()))
                .imageInfo(getImageInfo(post))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static ImageInfo getImageInfo(Post post) {
        return ImageInfo.builder()
                .filename(post.getImage().getFileName())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static FeedResponse getFeedResponse(Post post) {
        return FeedResponse.builder()
                .username(post.getPerson().getUsername())
                .lastPost(getPostInfo(post))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
