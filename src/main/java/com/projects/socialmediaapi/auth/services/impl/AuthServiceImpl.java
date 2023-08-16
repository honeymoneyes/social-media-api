package com.projects.socialmediaapi.auth.services.impl;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.JwtResponse;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.AuthService;
import com.projects.socialmediaapi.security.jwt.utils.JwtUtils;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.utils.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.projects.socialmediaapi.security.jwt.utils.MapUtils.toClaims;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public MessageResponse register(RegisterRequest request) {
        Person person = personMapper.toPerson(request);
        person.setPassword(passwordEncoder.encode(request.password()));
        personRepository.save(person);

        return MessageResponse.builder()
                .message("User registered successfully!")
                .build();
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password());

        UserDetails userDetails = (UserDetails) authenticationManager
                .authenticate(authToken)
                .getPrincipal();

        Person person = personRepository
                .findByEmail(request.email())
                .get();

        String token = jwtUtils.generateToken(toClaims(person), userDetails);
        String refreshToken = jwtUtils.generateToken(toClaims(person), userDetails);

        return JwtResponse.builder()
                .token(token)
                .build();
    }
}
