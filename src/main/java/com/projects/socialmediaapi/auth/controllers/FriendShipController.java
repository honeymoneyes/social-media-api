package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.services.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class FriendShipController {
    // -----------------------------------------------------------------------------------------------------------------

    private final FriendshipService friendShipService;

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping("/friends/{userId}")
    public ResponseEntity<?> performShowFriends(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showFriends(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<?> performShowSubscribers(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showSubscribers(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> performFollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.follow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<?> performUnfollow(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.unfollow(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/accept/{userId}")
    public ResponseEntity<?> performAcceptRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.acceptRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/reject/{userId}")
    public ResponseEntity<?> performRejectRequest(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.rejectRequest(id));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping("/remove/{userId}")
    public ResponseEntity<?> performRemoveFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.removeFriend(id));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
