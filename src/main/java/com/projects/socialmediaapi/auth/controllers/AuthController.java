package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.auth.payload.responses.JwtResponse;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/sign-up")
    public ResponseEntity<MessageResponse> performRegister(@RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(201)
                .body(authServiceImpl.register(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> performLogin(@RequestBody LoginRequest request) {
        return ResponseEntity
                .status(200)
                .body(authServiceImpl.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> performRefresh(@RequestHeader("refresh-token") TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();

        return ResponseEntity
                .status(200)
                .body(authServiceImpl.login(request));
    }
}
