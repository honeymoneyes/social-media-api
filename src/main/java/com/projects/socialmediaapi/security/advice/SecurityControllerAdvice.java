package com.projects.socialmediaapi.security.advice;

import com.projects.socialmediaapi.security.exceptions.DuplicateLoginException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenExpirationException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.projects.socialmediaapi.security.constants.TokenConstants.DATE_TIME_FORMAT;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class SecurityControllerAdvice {
    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorDetails handleTokenRefreshException(RefreshTokenNotFoundException exception) {
        return ErrorDetails.builder()
                .status(NOT_FOUND.value())
                .error("REFRESH_TOKEN_NOT_EXIST")
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleDuplicateLoginException(DuplicateLoginException exception) {
        return ErrorDetails.builder()
                .status(CONFLICT.value())
                .error("DUPLICATE_LOGIN")
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenExpirationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorDetails handleRefreshTokenExpirationException(RefreshTokenExpirationException exception) {
        return ErrorDetails.builder()
                .status(UNAUTHORIZED.value())
                .error("UNAUTHORIZED")
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .message(exception.getMessage())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ErrorDetails.builder()
                .status(BAD_REQUEST.value())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH).
                        format(LocalDateTime.now()))
                .error("NOT_VALID")
                .message(exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(" | ")))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        return ErrorDetails.builder()
                .status(BAD_REQUEST.value())
                .error("HEADER_IS_NOT_SET")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleBadCredentialsException(BadCredentialsException exception) {
        return ErrorDetails.builder()
                .status(BAD_REQUEST.value())
                .error("BAD_CREDENTIALS")
                .message(exception.getMessage())
                .timestamp(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH)
                        .format(LocalDateTime.now()))
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
}
