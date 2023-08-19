package com.projects.socialmediaapi.auth.controllers;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.impl.AuthServiceImpl;
import com.projects.socialmediaapi.security.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.security.payload.responses.JwtResponse;
import com.projects.socialmediaapi.security.payload.responses.TokenRefreshResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.projects.socialmediaapi.security.constants.EndpointConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(MAIN_AUTH)
public class AuthController {
    // -----------------------------------------------------------------------------------------------------------------

    private final AuthServiceImpl authServiceImpl;

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_UP)
    public ResponseEntity<MessageResponse> performRegister(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(201)
                .body(authServiceImpl.register(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(SIGN_IN)
    public ResponseEntity<JwtResponse> performLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity
                .status(200)
                .body(authServiceImpl.login(request));
    }

    // -----------------------------------------------------------------------------------------------------------------

    @PostMapping(REFRESH_TOKEN)
    public ResponseEntity<TokenRefreshResponse> performRefresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity
                .ok(authServiceImpl.refresh(request));
    }

    // -----------------------------------------------------------------------------------------------------------------
}
