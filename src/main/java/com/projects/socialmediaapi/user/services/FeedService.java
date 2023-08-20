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
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.projects.socialmediaapi.user.constants.UserConstants.POST_PER_PAGE_IS_NOT_POSITIVE;
import static com.projects.socialmediaapi.user.constants.UserConstants.SUBSCRIPTIONS_NOT_FOUND;
import static com.projects.socialmediaapi.user.enums.SortStatus.ASC;
import static com.projects.socialmediaapi.user.enums.SortStatus.DESC;
import static com.projects.socialmediaapi.user.services.UserInteractionService.getTimestamp;

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

        List<Post> listLatestPostSubscriptions = getLatestPostSubscriptions(listSubscriptions);

        List<Post> sortListLatestPostSubscriptions =
                sortListLatestPostSubscriptions(listLatestPostSubscriptions, sortByTimestamp);

        List<FeedResponse> latestPostFeedResponse =
                getLatestPostFeedResponse(sortListLatestPostSubscriptions);

        PageImpl<FeedResponse> content = getPage(page, postsPerPage, sortListLatestPostSubscriptions, latestPostFeedResponse);

        return PageableResponse.builder()
                .totalPages(content.getTotalPages())
                .feed(content.getContent())
                .totalItems(content.getTotalElements())
                .currentPage(content.getNumber())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Create pagination and extract data for the current page*/
    private PageImpl<FeedResponse> getPage(Long page, Long postsPerPage,
                                           List<Post> sortListLatestPostSubscriptions,
                                           List<FeedResponse> latestPostFeedResponse) {
        PageRequest pageable = PageRequest.of(page.intValue(), postsPerPage.intValue());

        int totalPages = getTotalPages(postsPerPage, sortListLatestPostSubscriptions.size());

        if (page > totalPages) {
            throw new IllegalArgumentException("Requested page number is out of range.");
        }

        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), latestPostFeedResponse.size());

        List<FeedResponse> feedResponses = latestPostFeedResponse.subList(fromIndex, toIndex);

        return new PageImpl<>(feedResponses, pageable,
                latestPostFeedResponse.size());
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
        if (sortByTimestamp.equals(ASC.name())) {
            return getListAscendingSortedLatestPost(listLatestPostSubscriptions);
        } else if (sortByTimestamp.equals(DESC.name())) {
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
                .map(post -> FeedResponse.builder()
                        .username(post.getPerson().getUsername())
                        .lastPost(PostInfo.builder()
                                .title(post.getTitle())
                                .body(post.getBody())
                                .timestamp(getTimestamp(post.getTimestamp()))
                                .imageInfo(ImageInfo.builder()
                                        .filename(post.getImage().getFileName())
                                        .build())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get the latest posts from subscriptions*/
    private static List<Post> getLatestPostSubscriptions(List<Person> listSubscriptions) {
        return listSubscriptions
                .stream()
                .map(Person::getPosts)
                .map(functionPostsToPostCompareByGetTimestamp())
                .filter(Objects::nonNull)

                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Function<List<Post>, Post> functionPostsToPostCompareByGetTimestamp() {
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

}
