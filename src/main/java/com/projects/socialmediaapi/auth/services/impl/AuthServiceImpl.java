package com.projects.socialmediaapi.auth.services.impl;

import com.projects.socialmediaapi.auth.payload.requests.LoginRequest;
import com.projects.socialmediaapi.auth.payload.requests.RegisterRequest;
import com.projects.socialmediaapi.auth.payload.responses.MessageResponse;
import com.projects.socialmediaapi.auth.services.AuthService;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenNotFoundException;
import com.projects.socialmediaapi.security.jwt.utils.JwtUtils;
import com.projects.socialmediaapi.security.models.RefreshToken;
import com.projects.socialmediaapi.security.payload.requests.TokenRefreshRequest;
import com.projects.socialmediaapi.security.payload.responses.JwtResponse;
import com.projects.socialmediaapi.security.payload.responses.TokenRefreshResponse;
import com.projects.socialmediaapi.security.services.RefreshTokenService;
import com.projects.socialmediaapi.security.services.impl.PersonDetails;
import com.projects.socialmediaapi.user.exceptions.UserAlreadyExistException;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.models.Person;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.utils.mappers.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.projects.socialmediaapi.security.constants.AuthConstants.BEARER;
import static com.projects.socialmediaapi.security.constants.AuthConstants.REGISTERED_SUCCESS;
import static com.projects.socialmediaapi.security.constants.TokenConstants.REFRESH_TOKEN_NOT_FOUND;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_ALREADY_EXIST;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    // -----------------------------------------------------------------------------------------------------------------

    private final PersonMapper personMapper;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public MessageResponse register(RegisterRequest request) {
        userIfExist(request);
        Person person = personMapper.toPerson(request);
        person.setPassword(passwordEncoder.encode(request.password()));
        personRepository.save(person);

        return MessageResponse.builder()
                .message(REGISTERED_SUCCESS)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private void userIfExist(RegisterRequest request) {
        if (personRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public JwtResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken = createAuthToken(request);
        PersonDetails userDetails = authenticateAndFetchUserDetails(authToken);
        Person person = findPersonByEmail(request);
        String token = jwtUtils.generateToken(userDetails, person);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return getJwtResponse(token, refreshToken);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();
        return getTokenRefreshResponse(requestRefreshToken);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private TokenRefreshResponse getTokenRefreshResponse(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getPerson)
                .map(person -> buildTokenRefreshResponse(requestRefreshToken, person))
                .orElseThrow(() ->
                        new RefreshTokenNotFoundException(requestRefreshToken, REFRESH_TOKEN_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static UsernamePasswordAuthenticationToken createAuthToken(LoginRequest request) {
        return new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password());
    }

    // -----------------------------------------------------------------------------------------------------------------

    private Person findPersonByEmail(LoginRequest request) {
        return personRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    // -----------------------------------------------------------------------------------------------------------------

    private PersonDetails authenticateAndFetchUserDetails(UsernamePasswordAuthenticationToken authToken) {
            return (PersonDetails) authenticationManager
                        .authenticate(authToken)
                        .getPrincipal();

    }

    // -----------------------------------------------------------------------------------------------------------------

    private static JwtResponse getJwtResponse(String token, RefreshToken refreshToken) {
        return JwtResponse.builder()
                .token(token)
                .refresh(refreshToken.getToken())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    private TokenRefreshResponse buildTokenRefreshResponse(String requestRefreshToken, Person person) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(person.getEmail());
        String token = jwtUtils.generateToken(userDetails, person);
        return new TokenRefreshResponse(token, requestRefreshToken, BEARER);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
