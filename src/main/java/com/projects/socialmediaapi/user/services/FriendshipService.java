package com.projects.socialmediaapi.user.services;

import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;

import java.util.Set;

public interface FriendshipService {

    FriendShipResponse follow(Long receiverId);

    FriendShipResponse unfollow(Long userId);

    FriendShipResponse acceptRequest(Long subscriberId);

    FriendShipResponse rejectRequest(Long senderId);

    FriendShipResponse removeFriend(Long userId);

    Set<PersonResponse> showFriends(Long userId);

    Set<PersonResponse> showSubscribers(Long userId);
}
