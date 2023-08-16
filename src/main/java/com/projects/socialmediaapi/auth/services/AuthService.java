package com.projects.socialmediaapi.auth.services;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.security.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.security.payload.responses.JwtResponse;
import com.projects.socialmediaapi.security.payload.responses.TokenRefreshResponse;

public interface AuthService {
    MessageResponse register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    TokenRefreshResponse refresh(TokenRefreshRequest request);
}
