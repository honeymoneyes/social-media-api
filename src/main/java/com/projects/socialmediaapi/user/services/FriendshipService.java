package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.enums.RelationshipStatus;
import com.projects.socialmediaapi.user.exceptions.*;
import com.projects.socialmediaapi.user.models.Friendship;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.repositories.FriendshipRepository;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.projects.socialmediaapi.user.constants.RelationshipActionConstants.*;
import static com.projects.socialmediaapi.user.constants.UserConstants.*;
import static com.projects.socialmediaapi.user.enums.RelationshipStatus.*;
import static com.projects.socialmediaapi.user.enums.RequestStatus.*;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    // -----------------------------------------------------------------------------------------------------------------

    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserInteractionService userInteractionService;

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse follow(Long receiverId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(receiverId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        if (!subscribersOfOtherPerson(result).contains(loggedInPerson(result))) {
            subscribersOfOtherPerson(result).add(loggedInPerson(result));
        } else {
            throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST);
        }

        if (!subscribersOfLoggedInPerson(result).contains(otherPerson(result))) {
            Friendship followRequest = createFriendShipWithStatusPending(result);
            friendshipRepository.save(followRequest);
        } else {
            acceptRequest(receiverId);
        }

        personRepository.save(result.loggedInPerson());
        personRepository.save(result.otherPerson());
        return getFriendShipResponse(SIGNED_UP, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse unfollow(Long userId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        Friendship friendship = friendshipRepository
                .findBySenderAndReceiver(loggedInPerson(result), otherPerson(result))
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

        boolean areUsersFriends = areUsersFriends(result);

        if (subscribersOfOtherPerson(result).contains(loggedInPerson(result))) {
            if (areUsersFriends) {
                friendsOfOtherPerson(result).remove(loggedInPerson(result));
                subscribersOfOtherPerson(result).remove(loggedInPerson(result));
                friendsOfLoggedInPerson(result).remove(otherPerson(result));

                friendship.setRequestStatus(PENDING);

                personRepository.save(result.loggedInPerson());
                personRepository.save(result.otherPerson());
                friendshipRepository.save(friendship);

            } else {
                subscribersOfOtherPerson(result).remove(loggedInPerson(result));
                friendshipRepository.delete(friendship);
            }
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        return getFriendShipResponse(UNSUBSCRIBED, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse acceptRequest(Long subscriberId) {

        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(subscriberId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (friendship.getRequestStatus().equals(ACCEPTED)) {
            throw new UsersAlreadyFriendsException(USERS_ARE_FRIENDS);
        }

        if (subscribersOfLoggedInPerson(result).contains(otherPerson(result))) {
            friendsOfLoggedInPerson(result).add(otherPerson(result));
            subscribersOfOtherPerson(result).add(loggedInPerson(result));
            friendsOfOtherPerson(result).add(loggedInPerson(result));
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }


        friendship.setRequestStatus(ACCEPTED);

        personRepository.save(result.loggedInPerson());
        personRepository.save(result.otherPerson());
        friendshipRepository.save(friendship);

        return getFriendShipResponse(SET_FRIEND, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse rejectRequest(Long senderId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(senderId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        if (subscribersOfLoggedInPerson(result).contains(otherPerson(result))) {
            rejectRequest(result);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }
        return getFriendShipResponse(REQUEST_REJECTED, result);
    }

    // -----------------------------------------------------------------------------------------------------------------


    public FriendShipResponse removeFriend(Long userId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        if (areUsersFriends(result)) {
            friendsOfLoggedInPerson(result).remove(otherPerson(result));
            subscribersOfLoggedInPerson(result).remove(otherPerson(result));
            friendsOfOtherPerson(result).remove(loggedInPerson(result));

            Friendship friendship = getFriendshipBySenderAndReceiver(result);

            friendship.setRequestStatus(PENDING);

            personRepository.save(result.loggedInPerson());
            personRepository.save(result.otherPerson());
            friendshipRepository.save(friendship);
        } else {
            throw new FriendNotFoundException(FRIEND_NOT_FOUND);
        }

        return getFriendShipResponse(DELETED_FRIEND, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showFriends(Long userId) {
        Set<PersonResponse> emptySet = getEmptySet(USER_HAS_NOT_FRIENDS);

        Set<Person> friends = getRelationships(userId, FRIENDS);
        Set<PersonResponse> friendsResponses = getResponses(friends);

        return !friendsResponses.isEmpty() ? friendsResponses : emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showSubscribers(Long userId) {
        Set<PersonResponse> emptySet = getEmptySet(USER_HAS_NOT_SUBSCRIBERS);

        Set<Person> subscribers = getRelationships(userId, SUBSCRIBERS);
        Set<PersonResponse> friendsResponses = getResponses(subscribers);

        return !friendsResponses.isEmpty() ? friendsResponses : emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Reject request of set friends*/
    private void rejectRequest(UserInteractionService.Result result) {
        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (friendship.getRequestStatus().equals(PENDING)) {
            friendship.setRequestStatus(REJECTED);
            friendshipRepository.save(friendship);
        } else {
            throw new FriendshipRequestNotFoundException(FRIENDSHIP_REQUEST_NOT_FOUND);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Check users are friends*/
    public static boolean areUsersFriends(UserInteractionService.Result result) {
        return friendsOfLoggedInPerson(result)
                .contains(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get relationships using RelationshipStatus*/
    private Set<Person> getRelationships(Long userId, RelationshipStatus status) {
        return personRepository
                .findById(userId)
                .map(user -> status == FRIENDS ? user.getFriends() : user.getSubscribers())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get Set<PersonResponse> from Set<Person>*/
    private static Set<PersonResponse> getResponses(Set<Person> friends) {
        return friends.stream()
                .map(friend -> PersonResponse.builder()
                        .id(friend.getId())
                        .username(friend.getUsername())
                        .build())
                .collect(toSet());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get empty list with response*/
    public Set<PersonResponse> getEmptySet(String request) {
        Set<PersonResponse> emptySet = new HashSet<>();
        emptySet.add(PersonResponse.builder()
                .id(0L)
                .username(request)
                .build());

        return emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------
    /*Get friends of Logged in Person*/
    private static Set<Person> friendsOfLoggedInPerson(UserInteractionService.Result result) {
        return loggedInPerson(result).getFriends();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get friends of Other Person*/
    private static Set<Person> friendsOfOtherPerson(UserInteractionService.Result result) {
        return otherPerson(result).getFriends();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get subscribers of Logged in Person*/
    private static Set<Person> subscribersOfLoggedInPerson(UserInteractionService.Result result) {
        return loggedInPerson(result).getSubscribers();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get subscribers of Other Person*/
    private static Set<Person> subscribersOfOtherPerson(UserInteractionService.Result result) {
        return otherPerson(result).getSubscribers();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Create friendship with status PENDING*/
    private static Friendship createFriendShipWithStatusPending(UserInteractionService.Result result) {
        return Friendship.builder()
                .sender(loggedInPerson(result))
                .receiver(otherPerson(result))
                .requestStatus(PENDING)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Check loggedInPerson and other person aren't same user*/
    public static void areIdsFromSameUser(Person loggedInPerson, Person otherPerson) {
        if (Objects.equals(loggedInPerson.getId(), otherPerson.getId())) {
            throw new SelfActionException(SELF_ACTION);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Person loggedInPerson(UserInteractionService.Result result) {
        return result.loggedInPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Person otherPerson(UserInteractionService.Result result) {
        return result.otherPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Friendship getFriendshipBySenderAndReceiver(UserInteractionService.Result result) {
        return friendshipRepository
                .findBySenderAndReceiver(otherPerson(result), loggedInPerson(result))
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse getFriendShipResponse(String message, UserInteractionService.Result result) {
        return FriendShipResponse.builder()
                .message(String.format(message, otherPerson(result).getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
