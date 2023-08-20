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
import java.util.Set;
import java.util.stream.Collectors;

import static com.projects.socialmediaapi.user.constants.UserConstants.SUBSCRIPTIONS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final UserInteractionService userInteractionService;
    private final PersonRepository personRepository;

    public Set<FeedResponse> getSubscribersFeed() {
        Person person = userInteractionService.getLoggedUser();

        List<Person> listSubscriptions = personRepository
                .findBySubscribersContaining(person)
                .orElseThrow(() ->
                        new SubscriberNotFoundException(SUBSCRIPTIONS_NOT_FOUND));

        Set<Post> setLatestPostSubscriptions = listSubscriptions
                .stream()
                .map(Person::getPosts)
                .map(post -> post.stream()
                        .max(Comparator.comparing(Post::getTimestamp))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return setLatestPostSubscriptions
                .stream()
                .map(post -> FeedResponse.builder()
                        .username(post.getPerson().getUsername())
                        .lastPost(post)
                        .build())
                .collect(Collectors.toSet());
    }
}
