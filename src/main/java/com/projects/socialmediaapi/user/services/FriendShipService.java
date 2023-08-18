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

        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person otherPerson = personRepository
                .findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (!otherPerson.getSubscribers().contains(loggedInPerson)) {
            otherPerson.getSubscribers().add(loggedInPerson);
        } else {
            throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST);
        }
        personRepository.save(loggedInPerson);
        personRepository.save(otherPerson);
        return FriendShipResponse.builder()
                .message(String.format("You've signed up for %s", otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse unfollow(Long userId) {
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

        if (otherPerson.getSubscribers().contains(loggedInPerson)) {
            otherPerson.getSubscribers().remove(loggedInPerson);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        personRepository.save(loggedInPerson);
        personRepository.save(otherPerson);
        return FriendShipResponse.builder()
                .message(String.format("You unsubscribed from %s",otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse acceptFriend(Long subscriberId) {

        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person loggedInPerson = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person otherPerson = personRepository
                .findById(subscriberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (loggedInPerson.getSubscribers().contains(otherPerson)) {
            loggedInPerson.getFriends().add(otherPerson);
            otherPerson.getSubscribers().add(loggedInPerson);
            otherPerson.getFriends().add(loggedInPerson);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        personRepository.save(loggedInPerson);
        personRepository.save(otherPerson);

        return FriendShipResponse.builder()
                .message(String.format("You and %s are friends now.", otherPerson.getUsername()))
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


    // -----------------------------------------------------------------------------------------------------------------

    // -----------------------------------------------------------------------------------------------------------------
}
