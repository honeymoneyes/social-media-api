package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.*;
import com.projects.socialmediaapi.user.models.FriendshipRequest;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.repositories.FriendshipRepository;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static com.projects.socialmediaapi.user.constants.UserConstants.*;
import static com.projects.socialmediaapi.user.enums.RequestStatus.*;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse follow(Long receiverId) {
        Result result = getLoggedUserAndOtherUser(receiverId);

        areIdsFromSameUser(result.loggedInPerson, result.otherPerson);

        if (!result.otherPerson.getSubscribers().contains(result.loggedInPerson)) {
            result.otherPerson.getSubscribers().add(result.loggedInPerson);
        } else {
            throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST);
        }

        if (!result.loggedInPerson.getSubscribers().contains(result.otherPerson)) {
            FriendshipRequest followRequest = FriendshipRequest.builder()
                    .sender(result.loggedInPerson)
                    .receiver(result.otherPerson)
                    .requestStatus(PENDING)
                    .build();
            friendshipRepository.save(followRequest);
        } else {
            acceptRequest(receiverId);
        }

        personRepository.save(result.loggedInPerson);
        personRepository.save(result.otherPerson);
        return FriendShipResponse.builder()
                .message(String.format("You've signed up for %s", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse unfollow(Long userId) {
        Result result = getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(result.loggedInPerson, result.otherPerson);

        FriendshipRequest friendship = friendshipRepository
                .findBySenderAndReceiver(result.loggedInPerson, result.otherPerson)
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

        boolean areUsersFriends = isAreUsersFriends(result);

        if (result.otherPerson.getSubscribers().contains(result.loggedInPerson)) {
            if (areUsersFriends) {
                result.otherPerson.getFriends().remove(result.loggedInPerson);
                result.otherPerson.getSubscribers().remove(result.loggedInPerson);
                result.loggedInPerson.getFriends().remove(result.otherPerson);

                friendship.setRequestStatus(PENDING);

                personRepository.save(result.loggedInPerson());
                personRepository.save(result.otherPerson());
                friendshipRepository.save(friendship);

            } else {
                result.otherPerson.getSubscribers().remove(result.loggedInPerson);
                friendshipRepository.delete(friendship);
            }
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        return FriendShipResponse.builder()
                .message(String.format("You unsubscribed from %s", result.otherPerson().getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse acceptRequest(Long subscriberId) {

        Result result = getLoggedUserAndOtherUser(subscriberId);

        areIdsFromSameUser(result.loggedInPerson, result.otherPerson);

        FriendshipRequest friendship = friendshipRepository
                .findBySenderAndReceiver(result.otherPerson, result.loggedInPerson)
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

        if (friendship.getRequestStatus().equals(ACCEPTED)) {
            throw new UsersAlreadyFriendsException(USERS_ARE_FRIENDS);
        }

        if (result.loggedInPerson.getSubscribers().contains(result.otherPerson)) {
            result.loggedInPerson.getFriends().add(result.otherPerson);
            result.otherPerson.getSubscribers().add(result.loggedInPerson);
            result.otherPerson.getFriends().add(result.loggedInPerson);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }


        friendship.setRequestStatus(ACCEPTED);

        personRepository.save(result.loggedInPerson);
        personRepository.save(result.otherPerson);
        friendshipRepository.save(friendship);

        return FriendShipResponse.builder()
                .message(String.format("You and %s are friends now.", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse rejectRequest(Long senderId) {
        Result result = getLoggedUserAndOtherUser(senderId);

        areIdsFromSameUser(result.loggedInPerson, result.otherPerson);

        if (result.loggedInPerson.getSubscribers().contains(result.otherPerson)) {
            rejectRequest(result);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }
        return FriendShipResponse.builder()
                .message(String.format("Friendship request for user %s rejected", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse removeFriend(Long userId) {
        Result result = getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(result.loggedInPerson, result.otherPerson);

        if (result.loggedInPerson.getFriends().contains(result.otherPerson)) {
            result.loggedInPerson.getFriends().remove(result.otherPerson);
            result.loggedInPerson.getSubscribers().remove(result.otherPerson);
            result.otherPerson.getFriends().remove(result.loggedInPerson);

            FriendshipRequest friendship = friendshipRepository
                    .findBySenderAndReceiver(result.otherPerson, result.loggedInPerson)
                    .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

            friendship.setRequestStatus(PENDING);

            personRepository.save(result.loggedInPerson);
            personRepository.save(result.otherPerson);
            friendshipRepository.save(friendship);
        } else {
            throw new FriendNotFoundException(FRIEND_NOT_FOUND);
        }

        return FriendShipResponse.builder()
                .message(String.format("You deleted a friend named %s", result.otherPerson.getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showFriends(Long userId) {
        return personRepository
                .findById(userId)
                .map(Person::getFriends)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                .stream()
                .map(friend -> PersonResponse.builder()
                        .id(friend.getId())
                        .username(friend.getUsername())
                        .build())
                .collect(toSet());
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showSubscribers(Long userId) {
        return personRepository
                .findById(userId)
                .map(Person::getSubscribers)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                .stream()
                .map(subscriber -> PersonResponse.builder()
                        .id(subscriber.getId())
                        .username(subscriber.getUsername())
                        .build())
                .collect(toSet());
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

    private static void areIdsFromSameUser(Person loggedInPerson, Person otherPerson) {
        if (Objects.equals(loggedInPerson.getId(), otherPerson.getId())) {
            throw new SelfActionException(SELF_ACTION);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void rejectRequest(Result result) {
        FriendshipRequest friendship = friendshipRepository
                .findBySenderAndReceiver(result.otherPerson, result.loggedInPerson)
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

        if (friendship.getRequestStatus().equals(PENDING)) {
            friendship.setRequestStatus(REJECTED);
            friendshipRepository.save(friendship);
        } else {
            throw new FriendshipRequestNotFoundException(FRIENDSHIP_REQUEST_NOT_FOUND);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private record Result(Person loggedInPerson, Person otherPerson) {
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isAreUsersFriends(Result result) {
        return result.loggedInPerson
                .getFriends()
                .stream()
                .anyMatch(friend -> Objects.equals(friend.getId(), result.otherPerson.getId()));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
