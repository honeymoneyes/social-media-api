package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.services.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.projects.socialmediaapi.user.constants.FriendEndpointConstants.*;

@RestController
@RequestMapping(MAIN_FRIENDS)
@RequiredArgsConstructor
public class FriendShipController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FriendshipService friendShipService;

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_FRIENDS)
    public ResponseEntity<Set<PersonResponse>> performShowFriends(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showFriends(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(SHOW_SUBSCRIBERS)
    public ResponseEntity<Set<PersonResponse>> performShowSubscribers(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showSubscribers(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(FOLLOW)
    public ResponseEntity<FriendShipResponse> performFollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.follow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(UNFOLLOW)
    public ResponseEntity<FriendShipResponse> performUnfollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.unfollow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(ACCEPT_REQUEST_FRIEND)
    public ResponseEntity<FriendShipResponse> performAcceptRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.acceptRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(REJECT_REQUEST_FRIEND)
    public ResponseEntity<FriendShipResponse> performRejectRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.rejectRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @DeleteMapping(REMOVE_FRIEND)
    public ResponseEntity<FriendShipResponse> performRemoveFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.removeFriend(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
