package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.enums.RelationshipStatus;
import com.projects.socialmediaapi.user.enums.RequestStatus;
import com.projects.socialmediaapi.user.exceptions.*;
import com.projects.socialmediaapi.user.models.Friendship;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.repositories.FriendshipRepository;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.user.services.UserInteractionService.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.projects.socialmediaapi.user.constants.RelationshipActionConstants.*;
import static com.projects.socialmediaapi.user.constants.UserConstants.*;
import static com.projects.socialmediaapi.user.enums.RelationshipStatus.*;
import static com.projects.socialmediaapi.user.enums.RequestStatus.*;
import static com.projects.socialmediaapi.user.services.UserInteractionService.UserNotFoundException;
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

        Result result = checkUsersAreNotSameUserAndGetThem(receiverId);

        if (!areUsersFriends(result)) {
            if (isOtherPersonSubscriberOfLoggedInPerson(result)) {
                acceptRequest(receiverId);
            } else {
                if (!isLoggedInPersonSubscriberOfOtherPerson(result)) {
                    addToSubscribersOfOtherPersonLoggedInPerson(result);
                    createFriendshipAndSave(result);
                } else {
                    throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST);
                }
            }
        } else {
            throw new UsersAlreadyFriendsException(USERS_ARE_FRIENDS);
        }

        saveLoggedInPersonAndOtherPerson(result);
        return getFriendShipResponse(SIGNED_UP, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse unfollow(Long userId) {

        Result result = checkUsersAreNotSameUserAndGetThem(userId);

        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (isLoggedInPersonSubscriberOfOtherPerson(result)) {
            if (areUsersFriends(result)) {
                removeFriend(otherPerson(result).getId());
            } else {
                handleUnfollowForNonFriend(result, friendship);
            }
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        return getFriendShipResponse(UNSUBSCRIBED, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse acceptRequest(Long subscriberId) {

        Result result = checkUsersAreNotSameUserAndGetThem(subscriberId);

        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (areUsersFriends(result)) {
            throw new UsersAlreadyFriendsException(USERS_ARE_FRIENDS);
        }

        if (isOtherPersonSubscriberOfLoggedInPerson(result)) {
            addToFriendsAndSubscribersBothUsersAndSave(result, friendship);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        return getFriendShipResponse(SET_FRIEND, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Reject request for setting friends*/
    @Transactional
    public FriendShipResponse rejectRequest(Long senderId) {

        Result result = checkUsersAreNotSameUserAndGetThem(senderId);

        if (areUsersFriends(result)) {
            removeFriend(otherPerson(result).getId());
            return getFriendShipResponse(DELETED_FRIEND, result);
        }

        if (isOtherPersonSubscriberOfLoggedInPerson(result)) {
            rejectRequest(result);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        return getFriendShipResponse(REQUEST_REJECTED, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void rejectRequest(Result result) {
        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (isFriendshipPending(friendship)) {
            rejectRequestAndSave(friendship);
        } else {
            throw new FriendshipRequestNotFoundException(FRIENDSHIP_REQUEST_NOT_FOUND);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse removeFriend(Long userId) {

        Result result = checkUsersAreNotSameUserAndGetThem(userId);

        if (areUsersFriends(result)) {
            removeAllRelationshipsForLoggedInPerson(result);
        } else {
            throw new FriendNotFoundException(FRIEND_NOT_FOUND);
        }

        return getFriendShipResponse(DELETED_FRIEND, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showFriends(Long userId) {
        Set<PersonResponse> emptySet = getEmptySetWithRequest(USER_HAS_NOT_FRIENDS);

        Set<Person> friends = getRelationships(userId, FRIENDS);
        Set<PersonResponse> friendsResponses = getResponses(friends);

        return !friendsResponses.isEmpty() ? friendsResponses : emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public Set<PersonResponse> showSubscribers(Long userId) {
        Set<PersonResponse> emptySet = getEmptySetWithRequest(USER_HAS_NOT_SUBSCRIBERS);

        Set<Person> subscribers = getRelationships(userId, SUBSCRIBERS);
        Set<PersonResponse> friendsResponses = getResponses(subscribers);

        return !friendsResponses.isEmpty() ? friendsResponses : emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Check users are friends*/
    public static boolean areUsersFriends(Result result) {
        return friendsOfLoggedInPerson(result)
                .contains(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get relationships using RelationshipStatus*/
    private Set<Person> getRelationships(Long userId, RelationshipStatus status) {
        return personRepository
                .findById(userId)
                .map(getFriendsOrSubscribers(status))
                .orElseThrow(UserNotFoundException());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get Set<PersonResponse> from Set<Person>*/
    private static Set<PersonResponse> getResponses(Set<Person> friends) {
        return friends.stream()
                .map(FriendshipService::getPersonResponse)
                .collect(toSet());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get empty list with response*/
    public Set<PersonResponse> getEmptySetWithRequest(String request) {
        Set<PersonResponse> emptySet = new HashSet<>();
        emptySet.add(getRelationshipByStatus(request));
        return emptySet;
    }

    // -----------------------------------------------------------------------------------------------------------------
    /*Get friends of Logged in Person*/
    private static Set<Person> friendsOfLoggedInPerson(Result result) {
        return loggedInPerson(result).getFriends();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get friends of Other Person*/
    private static Set<Person> friendsOfOtherPerson(Result result) {
        return otherPerson(result).getFriends();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get subscribers of Logged in Person*/
    private static Set<Person> subscribersOfLoggedInPerson(Result result) {
        return loggedInPerson(result).getSubscribers();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Get subscribers of Other Person*/
    private static Set<Person> subscribersOfOtherPerson(Result result) {
        return otherPerson(result).getSubscribers();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Create friendship with status PENDING*/
    private static Friendship createFriendShipWithStatusPending(Result result) {
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

    private static Person loggedInPerson(Result result) {
        return result.loggedInPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Person otherPerson(Result result) {
        return result.otherPerson();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Friendship getFriendshipBySenderAndReceiver(Result result) {
        return friendshipRepository
                .findBySenderAndReceiver(otherPerson(result), loggedInPerson(result))
                .orElseThrow(() -> new FriendshipRequestNotFoundException(FRIENDSHIP_NOT_FOUND));

    }

    // -----------------------------------------------------------------------------------------------------------------

    public FriendShipResponse getFriendShipResponse(String message, Result result) {
        return FriendShipResponse.builder()
                .message(String.format(message, otherPerson(result).getUsername()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void createFriendshipAndSave(Result result) {
        Friendship followRequest = createFriendShipWithStatusPending(result);
        friendshipRepository.save(followRequest);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isLoggedInPersonSubscriberOfOtherPerson(Result result) {
        return subscribersOfOtherPerson(result).contains(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isOtherPersonSubscriberOfLoggedInPerson(Result result) {
        return subscribersOfLoggedInPerson(result).contains(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void saveLoggedInPersonAndOtherPerson(Result result) {
        personRepository.save(result.loggedInPerson());
        personRepository.save(result.otherPerson());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromFriendsLoggedInPersonOtherPerson(Result result) {
        friendsOfLoggedInPerson(result).remove(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromSubscribersOtherPersonLoggedInPerson(Result result) {
        subscribersOfOtherPerson(result).remove(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromFriendsOtherPersonLoggedInPerson(Result result) {
        friendsOfOtherPerson(result).remove(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setFriendshipStatus(Friendship friendship, RequestStatus status) {
        friendship.setRequestStatus(status);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isFriendshipPending(Friendship friendship) {
        return friendship.getRequestStatus().equals(PENDING);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void removeAllRelationshipsForLoggedInPerson(Result result) {
        removeFromFriendsLoggedInPersonOtherPerson(result);
        removeFromSubscribersOtherPersonLoggedInPerson(result);
        removeFromFriendsOtherPersonLoggedInPerson(result);

        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        setFriendshipStatusPending(friendship);

        saveUsersAndFriendship(result, friendship);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void addToSubscribersOfOtherPersonLoggedInPerson(Result result) {
        subscribersOfOtherPerson(result).add(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void handleUnfollowForNonFriend(Result result, Friendship friendship) {
        removeFromSubscribersOtherPersonLoggedInPerson(result);
        friendshipRepository.delete(friendship);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void addToFriendsAndSubscribersBothUsersAndSave(Result result, Friendship friendship) {
        addToFriendsOfLoggedInPersonOtherPerson(result);
        addToSubscribersOfOtherPersonLoggedInPerson(result);
        addToFriendsOfOtherPersonLoggedInPerson(result);

        setFriendshipStatusAccepted(friendship);

        saveUsersAndFriendship(result, friendship);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void rejectRequestAndSave(Friendship friendship) {
        setFriendshipStatusRejected(friendship);
        friendshipRepository.save(friendship);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Result checkUsersAreNotSameUserAndGetThem(Long userId) {
        Result result = userInteractionService.getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));
        return result;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Check users are subscribers*/
    public static boolean areUsersSubscribers(Result result) {
        return subscribersOfLoggedInPerson(result)
                       .contains(otherPerson(result)) &&
               subscribersOfOtherPerson(result)
                       .contains(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setFriendshipStatusRejected(Friendship friendship) {
        setFriendshipStatus(friendship, REJECTED);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setFriendshipStatusPending(Friendship friendship) {
        setFriendshipStatus(friendship, PENDING);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setFriendshipStatusAccepted(Friendship friendship) {
        setFriendshipStatus(friendship, ACCEPTED);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void saveUsersAndFriendship(Result result, Friendship friendship) {
        saveLoggedInPersonAndOtherPerson(result);
        friendshipRepository.save(friendship);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void addToFriendsOfOtherPersonLoggedInPerson(Result result) {
        friendsOfOtherPerson(result).add(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void addToFriendsOfLoggedInPersonOtherPerson(Result result) {
        friendsOfLoggedInPerson(result).add(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PersonResponse getPersonResponse(Person friend) {
        return PersonResponse.builder()
                .id(friend.getId())
                .username(friend.getUsername())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static PersonResponse getRelationshipByStatus(String request) {
        return PersonResponse.builder()
                .id(0L)
                .username(request)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static Function<Person, Set<Person>> getFriendsOrSubscribers(RelationshipStatus status) {
        return user -> status == FRIENDS ? user.getFriends() : user.getSubscribers();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
