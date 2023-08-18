package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.SubscriberNotFoundException;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.projects.socialmediaapi.user.constants.UserConstants.SUBSCRIBER_NOT_FOUND;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse addFriend(Long receiverId) {

        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person sender = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person receiver = personRepository
                .findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        receiver.getSubscribers().add(sender);
        System.out.println(receiver.getSubscribers());
        personRepository.save(sender);
        personRepository.save(receiver);
        return FriendShipResponse.builder()
                .message("A user named - " + sender.getUsername() + " subscribed to a user named " + receiver.getUsername())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse acceptFriend(Long subscriberId) {

        PersonDetails personDetails = (PersonDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Person person = personRepository
                .findById(personDetails.getId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Person subscriber = personRepository
                .findById(subscriberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        if (person.getSubscribers().contains(subscriber)) {
            person.getFriends().add(subscriber);
            subscriber.getSubscribers().add(person);
            subscriber.getFriends().add(person);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        personRepository.save(person);
        personRepository.save(subscriber);

        return FriendShipResponse.builder()
                .message("A user named - " + person.getUsername() + " add friend with a user named " + subscriber.getUsername())
                .build();
    }

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
}
