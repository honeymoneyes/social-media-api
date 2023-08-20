package com.projects.socialmediaapi.security.advice;

import com.projects.socialmediaapi.security.exceptions.DuplicateLoginException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenExpirationException;
import com.projects.socialmediaapi.security.exceptions.RefreshTokenNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.projects.socialmediaapi.user.services.UserInteractionService.getErrorDetails;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class SecurityControllerAdvice {
    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorDetails handleTokenRefreshException(RefreshTokenNotFoundException exception) {
        return getErrorDetails(NOT_FOUND, "REFRESH_TOKEN_NOT_EXIST", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDetails handleDuplicateLoginException(DuplicateLoginException exception) {
        return getErrorDetails(CONFLICT, "DUPLICATE_LOGIN", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(RefreshTokenExpirationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorDetails handleRefreshTokenExpirationException(RefreshTokenExpirationException exception) {
        return getErrorDetails(UNAUTHORIZED, "UNAUTHORIZED", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return getErrorDetails(BAD_REQUEST, "NOT_VALID", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleMissingRequestHeaderException(MissingRequestHeaderException exception) {
        return getErrorDetails(BAD_REQUEST, "HEADER_IS_NOT_SET", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDetails handleBadCredentialsException(BadCredentialsException exception) {
        return getErrorDetails(BAD_REQUEST, "BAD_CREDENTIALS", exception);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
