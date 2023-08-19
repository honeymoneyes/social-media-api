package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.FriendShipResponse;
import com.projects.socialmediaapi.user.payload.responses.PersonResponse;
import com.projects.socialmediaapi.user.services.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class FriendShipController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FriendshipService friendShipService;

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping("/friends/{userId}")
    public ResponseEntity<Set<PersonResponse>> performShowFriends(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showFriends(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<Set<PersonResponse>> performShowSubscribers(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showSubscribers(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/follow/{userId}")
    public ResponseEntity<FriendShipResponse> performFollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.follow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<FriendShipResponse> performUnfollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.unfollow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/accept/{userId}")
    public ResponseEntity<FriendShipResponse> performAcceptRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.acceptRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/reject/{userId}")
    public ResponseEntity<FriendShipResponse> performRejectRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.rejectRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/remove/{userId}")
    public ResponseEntity<FriendShipResponse> performRemoveFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.removeFriend(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
