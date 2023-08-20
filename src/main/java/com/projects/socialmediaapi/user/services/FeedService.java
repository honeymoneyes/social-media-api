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
import java.util.stream.Collectors;

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

    private PageImpl<FeedResponse> getPage(Long page, Long postsPerPage, List<Post> sortListLatestPostSubscriptions, List<FeedResponse> latestPostFeedResponse) {
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

    private int getTotalPages(Long postsPerPage, int totalPosts) {
        return (int) Math.ceil((double) totalPosts / postsPerPage);
    }

    // -----------------------------------------------------------------------------------------------------------------

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

    private static List<Post> getListDescendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .toList();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static List<Post> getListAscendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp))
                .toList();
    }

    // -----------------------------------------------------------------------------------------------------------------

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

    private static List<Post> getLatestPostSubscriptions(List<Person> listSubscriptions) {
        return listSubscriptions
                .stream()
                .map(Person::getPosts)
                .map(post -> post.stream()
                        .max(Comparator.comparing(Post::getTimestamp))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private List<Person> getSubscriptions(Person person) {
        return personRepository
                .findBySubscribersContaining(person)
                .orElseThrow(() ->
                        new SubscriberNotFoundException(SUBSCRIPTIONS_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

}
