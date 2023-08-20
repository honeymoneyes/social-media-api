package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.exceptions.SubscriberNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.models.Post;
import com.projects.socialmediaapi.user.payload.responses.FeedResponse;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.projects.socialmediaapi.user.constants.UserConstants.SUBSCRIPTIONS_NOT_FOUND;
import static com.projects.socialmediaapi.user.enums.SortStatus.*;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final UserInteractionService userInteractionService;
    private final PersonRepository personRepository;

    public List<FeedResponse> getSubscribersFeed(Long page, Long postsPerPage, String sortByTimestamp) {
        Person person = userInteractionService.getLoggedUser();

        List<Person> listSubscriptions = getSubscriptions(person);

        List<Post> listLatestPostSubscriptions = getLatestPostSubscriptions(listSubscriptions);

        if (sortByTimestamp.equals(NOT.name())) {
            return getLatestPostFeedResponse(listLatestPostSubscriptions);
        } else if(sortByTimestamp.equals(ASC.name())) {
            return getLatestPostFeedResponse(
                    getListAscendingSortedLatestPost(listLatestPostSubscriptions));
        } else if(sortByTimestamp.equals(DESC.name())) {
            return getLatestPostFeedResponse(
                    getListDescendingSortedLatestPost(listLatestPostSubscriptions));
        }

    }

    private static List<Post> getListDescendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .toList();
    }

    private static List<Post> getListAscendingSortedLatestPost(List<Post> listLatestPostSubscriptions) {
        return listLatestPostSubscriptions.stream()
                .sorted(Comparator.comparing(Post::getTimestamp))
                .toList();
    }

    private static List<FeedResponse> getLatestPostFeedResponse(List<Post> setLatestPostSubscriptions) {
        return setLatestPostSubscriptions
                .stream()
                .map(post -> FeedResponse.builder()
                        .username(post.getPerson().getUsername())
                        .lastPost(post)
                        .build())
                .collect(Collectors.toList());
    }

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

    private List<Person> getSubscriptions(Person person) {
        return personRepository
                .findBySubscribersContaining(person)
                .orElseThrow(() ->
                        new SubscriberNotFoundException(SUBSCRIPTIONS_NOT_FOUND));
    }
}
