package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.services.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class FriendShipController {

    private final FriendShipService friendShipService;

    @GetMapping("/friends/{userId}")
    public ResponseEntity<?> performShowFriends(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showFriends(id));
    }

    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<?> performShowSubscribers(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.showSubscribers(id));
    }

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> performAddFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.addFriend(id));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<?> performAcceptFriend(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(friendShipService.acceptFriend(id));
    }
}
