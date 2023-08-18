package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.SubscriberAlreadyExistException;
import com.projects.socialmediaapi.user.exceptions.SubscriberNotFoundException;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.projects.socialmediaapi.user.constants.UserConstants.*;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse follow(Long receiverId) {

        Result result = getLoggedUserAndOtherUser(receiverId);

        if (!result.loggedInPerson.getSubscribers().contains(result.otherPerson)) {
            result.loggedInPerson.getSubscribers().add(result.otherPerson);
        } else {
            throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST);
        }
        personRepository.save(result.loggedInPerson);
        personRepository.save(result.otherPerson);
        return FriendShipResponse.builder()
                .message(String.format("You've signed up for %s", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse unfollow(Long userId) {
        Result result = getLoggedUserAndOtherUser(userId);

        if (result.loggedInPerson().getSubscribers().contains(result.otherPerson())) {
            result.loggedInPerson().getSubscribers().remove(result.otherPerson());
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        personRepository.save(result.loggedInPerson);
        personRepository.save(result.otherPerson);
        return FriendShipResponse.builder()
                .message(String.format("You unsubscribed from %s",result.otherPerson().getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse acceptFriend(Long subscriberId) {

        Result result = getLoggedUserAndOtherUser(subscriberId);

        if (result.loggedInPerson.getSubscribers().contains(result.otherPerson)) {
            result.loggedInPerson.getFriends().add(result.otherPerson);
            result.otherPerson.getSubscribers().add(result.loggedInPerson);
            result.otherPerson.getFriends().add(result.loggedInPerson);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        personRepository.save(result.loggedInPerson);
        personRepository.save(result.otherPerson);

        return FriendShipResponse.builder()
                .message(String.format("You and %s are friends now.", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<Person> showFriends(Long userId) {
        return personRepository
                .findById(userId)
                .map(Person::getFriends)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<Person> showSubscribers(Long userId) {
        return personRepository
                .findById(userId)
                .map(Person::getSubscribers)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Result getLoggedUserAndOtherUser(Long userId) {
        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person otherPerson = personRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        return new Result(loggedInPerson, otherPerson);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private record Result(Person loggedInPerson, Person otherPerson) {
    }
    // -----------------------------------------------------------------------------------------------------------------
}
