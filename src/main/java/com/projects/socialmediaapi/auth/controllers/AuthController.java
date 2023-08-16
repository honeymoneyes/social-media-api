package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.impl.AuthServiceImpl;
import com.projects.socialmediaapi.security.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.security.payload.responses.JwtResponse;
import com.projects.socialmediaapi.security.payload.responses.TokenRefreshResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.projects.socialmediaapi.security.constants.AuthConstants.REFRESH_HEADER;
import static com.projects.socialmediaapi.security.constants.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_AUTH)
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping(SIGN_UP)
    public ResponseEntity<MessageResponse> performRegister(@RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(201)
                .body(authServiceImpl.register(request));
    }

    @PostMapping(SIGN_IN)
    public ResponseEntity<JwtResponse> performLogin(@RequestBody LoginRequest request) {
        return ResponseEntity
                .status(200)
                .body(authServiceImpl.login(request));
    }

    @PostMapping(REFRESH)
    public ResponseEntity<TokenRefreshResponse> performRefresh(@RequestHeader(REFRESH_HEADER) TokenRefreshRequest request) {
        return ResponseEntity
                .ok(authServiceImpl.refresh(request));
    }
}
