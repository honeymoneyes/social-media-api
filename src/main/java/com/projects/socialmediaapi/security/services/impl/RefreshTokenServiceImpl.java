package com.projects.socialmediaapi.security.services.impl;

import com.projects.socialmediaapi.security.constants.TokenConstants;
import com.projects.socialmediaapi.security.exceptions.TokenRefreshException;
import com.projects.socialmediaapi.security.models.RefreshToken;
import com.projects.socialmediaapi.security.repositories.RefreshTokenRepository;
import com.projects.socialmediaapi.security.services.RefreshTokenService;
import com.projects.socialmediaapi.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PersonRepository personRepository;
    @Value("${jwt.refreshExpirationMs}")
    private long refreshTokenDurationMs;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .person(personRepository
                        .findById(userId)
                        .get())
                .expirationDate(ZonedDateTime
                        .now()
                        .toInstant()
                        .plusMillis(refreshTokenDurationMs))
                .token(UUID
                        .randomUUID()
                        .toString())
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), TokenConstants.TOKEN_EXPIRED);
        }

        return token;
    }

    @Override
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.delet(personRepository.findById(userId).get());
    }
}
