package com.projects.socialmediaapi.security.services.impl;

import com.projects.socialmediaapi.security.exceptions.DuplicateLoginException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenExpirationException;
import com.projects.socialmediaapi.security.models.RefreshToken;
import com.projects.socialmediaapi.security.repositories.RefreshTokenRepository;
import com.projects.socialmediaapi.security.services.RefreshTokenService;
import com.projects.socialmediaapi.user.exceptions.UserNotFoundException;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import com.projects.socialmediaapi.user.services.UserInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.projects.socialmediaapi.security.constants.AuthConstants.LOGIN_ALREADY_COMPLETED;
import static com.projects.socialmediaapi.security.constants.TokenConstants.REFRESH_TOKEN_EXPIRED;
import static com.projects.socialmediaapi.user.constants.UserConstants.USER_NOT_FOUND;
import static com.projects.socialmediaapi.user.services.UserInteractionService.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    // -----------------------------------------------------------------------------------------------------------------

    private final RefreshTokenRepository refreshTokenRepository;
    private final PersonRepository personRepository;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshTokenDurationMs;

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = refreshTokenBuild(userId);

        try {
            refreshTokenRepository.save(refreshToken);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateLoginException(LOGIN_ALREADY_COMPLETED);
        }

        return refreshToken;
    }

    // -----------------------------------------------------------------------------------------------------------------

    private RefreshToken refreshTokenBuild(Long userId) {
        return RefreshToken.builder()
                .person((personRepository
                        .findById(userId)
                        .orElseThrow(UserNotFoundException())))
                .expirationDate(ZonedDateTime
                        .now()
                        .toInstant()
                        .plusMillis(refreshTokenDurationMs))
                .token(UUID
                        .randomUUID()
                        .toString())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpirationException(
                    REFRESH_TOKEN_EXPIRED);
        }
        return token;
    }

    // -----------------------------------------------------------------------------------------------------------------
}
