package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;
import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/message/{userId}")
    public ResponseEntity<TextMessageResponse> performSendMessage(@PathVariable("userId") Long id,
                                                                  @RequestBody TextMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(id, request));
    }

    @GetMapping("/chat/{userId}")
    public ResponseEntity<List<TextMessageResponse>> performGetChat(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageService.getChat(userId));
    }
}
