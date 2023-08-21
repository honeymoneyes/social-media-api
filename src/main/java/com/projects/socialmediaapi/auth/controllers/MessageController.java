package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.user.payload.requests.TextMessageRequest;
import com.projects.socialmediaapi.user.payload.responses.TextMessageResponse;
import com.projects.socialmediaapi.user.services.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projects.socialmediaapi.user.constants.MessageEndpointConstants.*;

@RestController
@RequestMapping(MAIN_MESSAGE)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class MessageController {

    // -----------------------------------------------------------------------------------------------------------------

    private final MessageService messageService;

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SEND_MESSAGE)
    public ResponseEntity<TextMessageResponse> performSendMessage(@PathVariable("userId") Long id,
                                                                  @RequestBody TextMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(id, request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @GetMapping(GET_CHAT)
    public ResponseEntity<List<TextMessageResponse>> performGetChat(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageService.getChat(userId));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
