package com.projects.socialmediaapi.auth.services;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.JwtResponse;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;

public interface AuthService {
    MessageResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}
