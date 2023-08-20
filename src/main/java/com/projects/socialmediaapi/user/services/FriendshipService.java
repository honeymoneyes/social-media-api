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

        if (!areUsersFriends(result)) { // Если не друзья
            if (isOtherPersonSubscriberOfLoggedInPerson(result)) { // Если другой пользователь мой подписчик
                acceptRequest(receiverId); // Организовать дружбу
            } else { // Если другой пользователь не мой подписчик
                if (!isLoggedInPersonSubscriberOfOtherPerson(result)) { // Если я не подписчик второго пользователя
                    subscribersOfOtherPerson(result).add(loggedInPerson(result)); // Добавиться в подписчики к нему
                    createFriendshipAndSave(result); // Отправить запрос подписки
                } else { // Если я подписчик второго пользователя
                    throw new SubscriberAlreadyExistException(SUBSCRIBER_ALREADY_EXIST); // Я уже подписан на него
                }
            }
        } else { // Если друзья
            throw new FriendAlreadyExistException(YOU_ARE_FRIENDS);
        }

        saveLoggedInPersonAndOtherPerson(result);
        return getFriendShipResponse(SIGNED_UP, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse unfollow(Long userId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (isLoggedInPersonSubscriberOfOtherPerson(result)) {
            if (areUsersFriends(result)) {

                removeFromFriendsOtherPersonLoggenInPerson(result);
                removeFromSubscribersOtherPersonLoggedInPerson(result);
                removeFromFriendsLoggedInPersonOtherPerson(result);

                setFriendshipStatus(friendship, PENDING);

                saveLoggedInPersonAndOtherPerson(result);
                friendshipRepository.save(friendship);

            } else {
                removeFromSubscribersOtherPersonLoggedInPerson(result);
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

        if (areUsersFriends(result)) {
            throw new UsersAlreadyFriendsException(USERS_ARE_FRIENDS);
        }

        if (isOtherPersonSubscriberOfLoggedInPerson(result)) {
            friendsOfLoggedInPerson(result).add(otherPerson(result));
            subscribersOfOtherPerson(result).add(loggedInPerson(result));
            friendsOfOtherPerson(result).add(loggedInPerson(result));
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }

        setFriendshipStatus(friendship, ACCEPTED);

        saveLoggedInPersonAndOtherPerson(result);
        friendshipRepository.save(friendship);

        return getFriendShipResponse(SET_FRIEND, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse rejectRequest(Long senderId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(senderId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        if (isOtherPersonSubscriberOfLoggedInPerson(result)) {
            rejectRequest(result);
        } else {
            throw new SubscriberNotFoundException(SUBSCRIBER_NOT_FOUND);
        }
        return getFriendShipResponse(REQUEST_REJECTED, result);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /*Reject request of set friends*/
    private void rejectRequest(UserInteractionService.Result result) {
        Friendship friendship = getFriendshipBySenderAndReceiver(result);

        if (isFriendshipPending(friendship)) {
            setFriendshipStatus(friendship, REJECTED);
            friendshipRepository.save(friendship);
        } else {
            throw new FriendshipRequestNotFoundException(FRIENDSHIP_REQUEST_NOT_FOUND);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Transactional
    public FriendShipResponse removeFriend(Long userId) {
        UserInteractionService.Result result = userInteractionService.getLoggedUserAndOtherUser(userId);

        areIdsFromSameUser(loggedInPerson(result), otherPerson(result));

        if (areUsersFriends(result)) {
            removeFromFriendsLoggedInPersonOtherPerson(result);
            removeFromSubscribersOfLoggedInPersonOtherPerson(result);
            removeFromFriendsOtherPersonLoggenInPerson(result);

            Friendship friendship = getFriendshipBySenderAndReceiver(result);

            setFriendshipStatus(friendship, PENDING);

            saveLoggedInPersonAndOtherPerson(result);
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

    /*Check users are friends*/
    public static boolean areUsersFriends(UserInteractionService.Result result) {
        return friendsOfLoggedInPerson(result)
                .contains(otherPerson(result));
    }


    // -----------------------------------------------------------------------------------------------------------------

    /*Check users are subscribers*/
    public static boolean areUsersSubscribers(UserInteractionService.Result result) {
        return subscribersOfLoggedInPerson(result)
                       .contains(otherPerson(result)) &&
               subscribersOfOtherPerson(result)
                       .contains(loggedInPerson(result));
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

    private void createFriendshipAndSave(UserInteractionService.Result result) {
        Friendship followRequest = createFriendShipWithStatusPending(result);
        friendshipRepository.save(followRequest);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isLoggedInPersonSubscriberOfOtherPerson(UserInteractionService.Result result) {
        return subscribersOfOtherPerson(result).contains(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isOtherPersonSubscriberOfLoggedInPerson(UserInteractionService.Result result) {
        return subscribersOfLoggedInPerson(result).contains(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void saveLoggedInPersonAndOtherPerson(UserInteractionService.Result result) {
        personRepository.save(result.loggedInPerson());
        personRepository.save(result.otherPerson());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromFriendsLoggedInPersonOtherPerson(UserInteractionService.Result result) {
        friendsOfLoggedInPerson(result).remove(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromSubscribersOtherPersonLoggedInPerson(UserInteractionService.Result result) {
        subscribersOfOtherPerson(result).remove(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromFriendsOtherPersonLoggenInPerson(UserInteractionService.Result result) {
        friendsOfOtherPerson(result).remove(loggedInPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void setFriendshipStatus(Friendship friendship, RequestStatus status) {
        friendship.setRequestStatus(status);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static void removeFromSubscribersOfLoggedInPersonOtherPerson(UserInteractionService.Result result) {
        subscribersOfLoggedInPerson(result).remove(otherPerson(result));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static boolean isFriendshipPending(Friendship friendship) {
        return friendship.getRequestStatus().equals(PENDING);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
